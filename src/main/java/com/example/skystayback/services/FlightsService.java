package com.example.skystayback.services;

import com.example.skystayback.config.ThymeleafConfig;
import com.example.skystayback.dtos.common.*;
import com.example.skystayback.dtos.flights.*;
import com.example.skystayback.enums.StatusOrder;
import com.example.skystayback.models.FlightSeatStatus;
import com.example.skystayback.models.OrderFlight;
import com.example.skystayback.models.SeatBooking;
import com.example.skystayback.models.User;
import com.example.skystayback.repositories.FlightRepository;
import com.example.skystayback.repositories.FlightSeatStatusRepository;
import com.example.skystayback.repositories.OrderFlightRepository;
import com.example.skystayback.repositories.SeatBookingRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.skystayback.dtos.meal.MealVO;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.itextpdf.html2pdf.HtmlConverter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class FlightsService {

    private final FlightRepository flightRepository;
    private final UserService userService;
    private final OrderFlightRepository orderFlightRepository;
    private final FlightSeatStatusRepository flightSeatStatusRepository;
    private final SeatBookingRepository seatBookingRepository;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    /**
     * Recupera todos los vuelos con paginación y filtros opcionales.
     * @param page Paginación de la consulta.
     * @param origin Filtro por origen del vuelo.
     * @param destination Filtro por destino del vuelo.
     * @param airline Filtro por aerolínea del vuelo.
     * @param price Filtro por precio del vuelo.
     * @return ResponsePaginatedVO con los vuelos encontrados y metadatos de paginación.
     */
    public ResponsePaginatedVO<FlightClientVO> getAllFlights(PageVO page, String origin, String destination, String airline, Float price) {
        try {
            Page<FlightClientVO> airplanePage = flightRepository.findAllClientFlightsWithFilters(page.toPageable(), origin, destination, airline, price);
            ResponsePaginatedVO<FlightClientVO> data = new ResponsePaginatedVO<>();
            data.setObjects(airplanePage.getContent());
            data.setHasNextPage(airplanePage.hasNext());
            data.setHasPreviousPage(airplanePage.hasPrevious());
            data.setCurrentPage(airplanePage.getNumber());
            data.setTotalPages(airplanePage.getTotalPages());
            data.setMessages(new MessageResponseVO("Vuelos recuperados con éxito.", 200, LocalDateTime.now()));
            return data;
        } catch (Exception e) {
            System.out.println("getAllFlights: " + e.getMessage());
            return new ResponsePaginatedVO<>(new MessageResponseVO("Error al recuperar los vuelos", 404, LocalDateTime.now()));
        }
    }

    /**
     * Mediante el código de vuelo, recupera toda la informacion relevante del vuelo.
     */
    public ResponseVO<AllDetailsFlightsVO> getFlightDetails(String flightCode) {
        try {
            AllDetailsFlightsProjection flight = flightRepository.findFlightDetailsById(flightCode).orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));
            AllDetailsFlightsVO flightParsed = new AllDetailsFlightsVO(flight);
            return new ResponseVO<>(new DataVO<>(flightParsed), new MessageResponseVO("Vuelo recuperado con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getFlightDetails: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar el vuelo", 404, LocalDateTime.now()));
        }
    }

    /**
     * Mediante en código de vuelo, recupera las comidas disponibles para el vuelo.
     */
    public ResponseVO<List<MealVO>> getMealsByFlightCode(String flightCode) {
        try {
            List<MealVO> meals = flightRepository.findMealsByFlightCode(flightCode);
            return new ResponseVO<>(new DataVO<>(meals), new MessageResponseVO("Comidas recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getMealsByFlightCode: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las comidas", 404, LocalDateTime.now()));
        }
    }

    /**
     * Mediante el código de vuelo, recupera las cabinas disponibles para el vuelo.
     */
    public ResponseVO<List<CabinFlightDetailsVO>> getCabinsForFlightByCode(String flightCode) {
        try {
            List<CabinFlightDetailsVO> cabins = flightRepository.getCabinsForFlightByCode(flightCode);

            for (CabinFlightDetailsVO cabin : cabins) {
                List<SeatVO> rawSeats = flightRepository.getSeatsForCabin(cabin.getId());

                Map<String, SeatVO> uniqueSeatsMap = new LinkedHashMap<>();
                for (SeatVO seat : rawSeats) {
                    String key = seat.getId() + "-" + seat.getSeatRow() + "-" + seat.getSeatColumn();
                    uniqueSeatsMap.put(key, seat);
                }

                cabin.setSeats(new ArrayList<>(uniqueSeatsMap.values()));
            }


            Map<String, CabinFlightDetailsVO> uniqueCabinsMap = new LinkedHashMap<>();
            for (CabinFlightDetailsVO cabin : cabins) {
                uniqueCabinsMap.put(String.valueOf(cabin.getSeatClass()), cabin);
            }

            List<CabinFlightDetailsVO> uniqueCabins = new ArrayList<>(uniqueCabinsMap.values());

            // Ordenar cabinas por seatClass
            uniqueCabins.sort(Comparator.comparing(CabinFlightDetailsVO::getSeatClass));

            return new ResponseVO<>(new DataVO<>(uniqueCabins), new MessageResponseVO("Cabinas recuperadas con éxito.", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("getCabinsForFlightByCode: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al recuperar las cabinas", 404, LocalDateTime.now()));
        }
    }


    /**
     * Permite comprar tickets de vuelo, reservando los asientos seleccionados.
     * @param flightCode Código del vuelo.
     * @param flightPurchaseDTO Lista de asientos a reservar y datos del pasajero.
     * @param token Token de autenticación del usuario.
     * @return ResponseVO con el resultado de la operación.
     */
    @Transactional
    public ResponseVO<Void> purchaseTicketsFlight(String flightCode, List<FlightPurchaseVO> flightPurchaseDTO, String token, String language) {
        try {
            Double totalAmount = 0.0;
            User user = userService.getUser(token);
            OrderFlight of = new OrderFlight();
            String code = userService.generateShortUuid();
            if (orderFlightRepository.existsByCode(code)) {
                code = userService.generateShortUuid();
            }
            of.setCode(code);
            of.setAmount(0.0);
            of.setDiscount(0.0);
            of.setStatus(StatusOrder.Paid);
            of.setBill(null);
            orderFlightRepository.save(of);

            for (FlightPurchaseVO purchase : flightPurchaseDTO) {
                FlightSeatStatus flightSeatStatus = flightSeatStatusRepository.findSeatByFlightCodeAndRowAndColumn(flightCode, purchase.getSeatRow(), purchase.getSeatColumn(), purchase.getSeatClass()).orElseThrow(() -> new RuntimeException("Asiento no encontrado"));
                System.out.println(flightSeatStatus + " - ");
                if (seatBookingRepository.existsByFlightSeatStatusAndEmailAndNifAndPhone(flightSeatStatus, purchase.getEmail(), purchase.getNif(), purchase.getPhone())) {
                    return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Ya existe una reserva con estos datos en el mismo vuelo.", 400, LocalDateTime.now()));
                }
                if (!flightSeatStatus.getState()) {
                    return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Asiento no encontrado", 404, LocalDateTime.now()));
                }
                flightSeatStatus.setState(false);
                flightSeatStatusRepository.save(flightSeatStatus);
                System.out.println(flightSeatStatus);
                SeatBooking seatBooking = new SeatBooking();
                seatBooking.setName(purchase.getName());
                seatBooking.setSurnames(purchase.getSurnames());
                seatBooking.setEmail(purchase.getEmail());
                seatBooking.setNif(purchase.getNif());
                seatBooking.setPhone(purchase.getPhone());
                seatBooking.setOrderFlight(of);
                seatBooking.setFlightSeatStatus(flightSeatStatus);
                seatBooking.setUser(user);
                seatBookingRepository.save(seatBooking);
                totalAmount += flightSeatStatus.getPrice();
            }
            of.setAmount(totalAmount * (1 - of.getDiscount()));
            ByteArrayOutputStream pdf = generateBillPdf(flightPurchaseDTO, of, flightCode, language);
            String htlmlContent = generateBillHtml(flightPurchaseDTO, of, flightCode, language);
            of.setBill(pdf.toByteArray());
            orderFlightRepository.save(of);
            sendInvoiceEmail(user.getEmail(), pdf, of.getCode(), language, htlmlContent);
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Asientos reservados y tickets de vuelo generados", 200, LocalDateTime.now()));
        } catch (Exception e) {
            System.out.println("purchaseFlight: " + e.getMessage());
            return new ResponseVO<>(new DataVO<>(), new MessageResponseVO("Error al comprar el vuelo", 404, LocalDateTime.now()));
        }
    }

    /**
     * Genera el contenido HTML de la factura de compra de los vuelos.
     * @param flightPurchaseDTO Lista de asientos comprados y datos del pasajero.
     * @param orderFlight Información del pedido de vuelo.
     * @param flightCode Código del vuelo.
     * @param language Idioma para el contenido de la factura.
     * @return String con el contenido HTML generado.
     */
    private String generateBillHtml(List<FlightPurchaseVO> flightPurchaseDTO, OrderFlight orderFlight, String flightCode, String language) {
        Double totalAmount = 0.0;
        FlightSeatStatus firstSeatStatus = null;

        for (FlightPurchaseVO purchase : flightPurchaseDTO) {
            FlightSeatStatus flightSeatStatus = flightSeatStatusRepository.findSeatByFlightCodeAndRowAndColumn(flightCode, purchase.getSeatRow(), purchase.getSeatColumn(), purchase.getSeatClass()).orElseThrow(() -> new RuntimeException("Asiento no encontrado"));
            totalAmount += flightSeatStatus.getPrice();

            if (firstSeatStatus == null) {
                firstSeatStatus = flightSeatStatus;
            }
        }

        if (firstSeatStatus == null) {
            throw new RuntimeException("No se encontraron asientos para generar la factura.");
        }

        Context context = new Context();

        List<Map<String, Object>> passengers = new ArrayList<>();
        for (FlightPurchaseVO passenger : flightPurchaseDTO) {
            Map<String, Object> passengerData = Map.of("name", passenger.getName(), "surnames", passenger.getSurnames(), "email", passenger.getEmail(), "nif", passenger.getNif(), "phone", passenger.getPhone(), "seatRow", passenger.getSeatRow(), "seatColumn", passenger.getSeatColumn(), "seatClass", passenger.getSeatClass(), "basePrice", flightSeatStatusRepository.findSeatByFlightCodeAndRowAndColumn(flightCode, passenger.getSeatRow(), passenger.getSeatColumn(), passenger.getSeatClass()).orElseThrow(() -> new RuntimeException("Asiento no encontrado")).getPrice());
            passengers.add(passengerData);
        }
        context.setVariable("invoice", Map.of("number", generateNumberOfBill(), "date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm")), "passengers", passengers));

        context.setVariable("flight", Map.of("code", firstSeatStatus.getFlight().getCode(), "airline", firstSeatStatus.getFlight().getAirline().getName(), "originCity", firstSeatStatus.getFlight().getDepartureAirport().getCity().getName(), "originAirport", firstSeatStatus.getFlight().getDepartureAirport().getName(), "destinationCity", firstSeatStatus.getFlight().getArrivalAirport().getCity().getName(), "destinationAirport", firstSeatStatus.getFlight().getArrivalAirport().getName(), "departure", firstSeatStatus.getFlight().getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm")), "arrival", firstSeatStatus.getFlight().getDateTimeArrival().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm"))));

        context.setVariable("order", Map.of("code", orderFlight.getCode(), "status", orderFlight.getStatus(), "basePrice", totalAmount, "discount", orderFlight.getDiscount(), "totalPrice", orderFlight.getAmount()));

        return language != null && language.equalsIgnoreCase("es") ? templateEngine.process("invoice-template", context) : templateEngine.process("invoice-template-en", context);
    }


    /**
     * Genera un PDF de la factura de compra de los vuelos.
     * @param flightPurchaseDTO Lista de asientos comprados y datos del pasajero.
     * @param orderFlight Información del pedido de vuelo.
     * @param flightCode Código del vuelo.
     * @param language Idioma para el contenido de la factura.
     * @return ByteArrayOutputStream con el contenido del PDF generado.
     */
    public ByteArrayOutputStream generateBillPdf(List<FlightPurchaseVO> flightPurchaseDTO, OrderFlight orderFlight, String flightCode, String language) {
        String htmlContent = generateBillHtml(flightPurchaseDTO, orderFlight, flightCode, language);
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);
        return pdfOutputStream;
    }

    /**
     * Envía un correo electrónico con la factura de compra en formato PDF.
     * @param recipientEmail Correo electrónico del destinatario.
     * @param pdf Contenido del PDF de la factura.
     * @param orderCode Código del pedido.
     * @param language Idioma del contenido del correo.
     */
    private void sendInvoiceEmail(String recipientEmail, ByteArrayOutputStream pdf, String orderCode, String language, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipientEmail);
            helper.setSubject(language != null && language.equalsIgnoreCase("es") ? "Factura de compra - Pedido " + orderCode : "Purchase Invoice - Order " + orderCode);
            helper.setText(language != null && language.equalsIgnoreCase("es") ? "Adjuntamos la factura de su compra. ¡Gracias por confiar en nosotros!" : "We have attached your purchase invoice. Thank you for trusting us!");
            helper.addAttachment((language != null && language.equalsIgnoreCase("es") ? "Factura_" : "Invoice_") + orderCode + ".pdf", new ByteArrayDataSource(pdf.toByteArray(), "application/pdf"));
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    /**
     * Genera un número de factura único basado en la fecha actual y un número aleatorio.
     * @return String con el número de factura generado.
     */
    private String generateNumberOfBill() {
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear()).substring(2);
        String month = String.format("%02d", today.getMonthValue());
        String day = String.format("%02d", today.getDayOfMonth());
        int randomFiveDigitNumber = (int) (Math.random() * 90000) + 10000;
        return day + month + year + randomFiveDigitNumber;
    }
}
