package com.example.skystayback.services.accommodation;

import com.example.skystayback.dtos.common.*;
import com.example.skystayback.enums.StatusOrder;
import com.example.skystayback.enums.StatusRoomBooking;
import com.example.skystayback.models.*;
import com.example.skystayback.repositories.*;
import com.example.skystayback.security.JwtService;
import com.example.skystayback.services.email.EmailService;
import com.example.skystayback.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GlobalService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private RoomBookingRepository roomBookingRepository;
    @Autowired
    private RoomAparmentBookingRepository roomAparmentBookingRepository;
    @Autowired
    private RoomApartmentRepository roomApartmentRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private OrderHotelRepository orderHotelRepository;
    @Autowired
    private OrderApartmentRepository orderApartmentRepository;
    @Autowired
    private RoomConfigurationHotelRepository roomConfigurationHotelRepository;
    @Autowired
    private RoomConfigurationApartmentRepository roomConfigurationApartmentRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TemplateEngine templateEngine;

    public ResponseVO<List<AccommodationResponseVO>> searchAccommodations(
            String destination,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer adults,
            Integer children,
            Integer rooms,
            String type) {

        List<AccommodationResponseVO> hotels = findByFilters(destination, checkIn, checkOut, adults, children, rooms, type);

        return ResponseVO.<List<AccommodationResponseVO>>builder()
                .response(new DataVO<>(hotels))
                .messages(new MessageResponseVO("Alojamientos encontrados", 200, LocalDateTime.now()))
                .build();
    }

    private List<AccommodationResponseVO> findByFilters(
            String destination,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer adults,
            Integer children,
            Integer rooms,
            String type) {

        List<AccommodationResponseVO> accommodations = new ArrayList<>();

        // Si no se especifica tipo o se solicitan hoteles, buscar hoteles
        if (type == null || "hotel".equalsIgnoreCase(type)) {
            List<AccommodationResponseVO> hotels = hotelRepository.findHotelsByDestination(destination);
            for (AccommodationResponseVO hotel : hotels) {
                hotel.setAccommodationType("hotel"); // Establecer el tipo
                List<RoomDetailsVO> availableRooms = hotelRepository.findAvailableRoomsByHotel(
                        hotel.getCode(), checkIn, checkOut, rooms, adults, children);
                hotel.setAvailableRooms(availableRooms);
                if (!availableRooms.isEmpty()) {
                    accommodations.add(hotel);
                }
            }
        }

        // Si no se especifica tipo o se solicitan apartamentos, buscar apartamentos
        if (type == null || "apartment".equalsIgnoreCase(type)) {
            List<AccommodationResponseVO> apartments = hotelRepository.findApartmentsByDestination(destination);
            for (AccommodationResponseVO apartment : apartments) {
                apartment.setAccommodationType("apartment"); // Establecer el tipo
                List<RoomDetailsVO> availableRooms = hotelRepository.findAvailableRoomsByApartment(
                        apartment.getCode(), checkIn, checkOut, rooms, adults, children);
                apartment.setAvailableRooms(availableRooms);
                if (!availableRooms.isEmpty()) {
                    accommodations.add(apartment);
                }
            }
        }

        return accommodations;
    }

    public List<String> getAllCities() {
        return hotelRepository.findAllCities();
    }

    public List<DestinationVO> getDestinations() {
        Pageable limit = PageRequest.of(0, 3);
        List<DestinationVO> hotels = hotelRepository.findRandomHotels(limit);
        List<DestinationVO> apartments = hotelRepository.findRandomApartments(limit);

        List<DestinationVO> combined = new ArrayList<>();
        combined.addAll(hotels);
        combined.addAll(apartments);

        if (combined.size() < 6) {
            if (hotels.size() < 3) {
                combined.addAll(hotelRepository.findRandomHotels(PageRequest.of(0, 3 - combined.size())));
            }
            if (apartments.size() < 3 && combined.size() < 3) {
                combined.addAll(hotelRepository.findRandomApartments(PageRequest.of(0, 3 - combined.size())));
            }
        }

        return combined.stream().limit(6).collect(Collectors.toList());
    }

    public List<DestinationVO> getTopRatedDestinations() {
        Pageable limit = PageRequest.of(0, 5);
        List<DestinationVO> topHotels = hotelRepository.findTopRatedHotels(limit);
        List<DestinationVO> topApartments = hotelRepository.findTopRatedApartments(limit);

        List<DestinationVO> combined = new ArrayList<>();
        combined.addAll(topHotels);
        combined.addAll(topApartments);

        int total = combined.size();

        if (total < 10) {
            if (topHotels.size() < 5) {
                int needed = 10 - total;
                List<DestinationVO> moreApartments = hotelRepository.findTopRatedApartments(PageRequest.of(0, needed));
                moreApartments.removeAll(topApartments);
                combined.addAll(moreApartments);
            }
            if (combined.size() < 10 && topApartments.size() < 3) {
                int needed = 10 - combined.size();
                List<DestinationVO> moreHotels = hotelRepository.findTopRatedHotels(PageRequest.of(0, needed));
                moreHotels.removeAll(topHotels);
                combined.addAll(moreHotels);
            }
        }

        if (topHotels.isEmpty()) {
            combined = hotelRepository.findTopRatedApartments(PageRequest.of(0, 10));
        }
        if (topApartments.isEmpty()) {
            combined = hotelRepository.findTopRatedHotels(PageRequest.of(0, 10));
        }

        return combined.stream().limit(10).collect(Collectors.toList());
    }

    public ResponseVO<AccommodationDetailVO> getAccommodationDetail(
            String code,
            String typeAccomodation,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer adults,
            Integer children,
            Integer rooms) {

        AccommodationDetailVO accommodationDetail;
        List<String> images;
        List<RoomDetailsVO> availableRooms;

        // Parámetros efectivos
        Integer effectiveAdults = adults != null ? adults : 1;
        Integer effectiveChildren = children != null ? children : 0;
        Integer effectiveRooms = rooms != null ? rooms : 1;

        if ("apartment".equalsIgnoreCase(typeAccomodation)) {
            accommodationDetail = hotelRepository.findApartmentDetailById(code);
            if (accommodationDetail == null) {
                return ResponseVO.<AccommodationDetailVO>builder()
                        .response(null)
                        .messages(new MessageResponseVO("No se encontró el alojamiento", 404, LocalDateTime.now()))
                        .build();
            }
            images = hotelRepository.findAllApartmentImages(code);
            availableRooms = hotelRepository.findAvailableRoomsByApartment(
                    code, checkIn, checkOut, effectiveRooms, effectiveAdults, effectiveChildren);
            accommodationDetail.setAccommodationType("apartment");
        } else { // Por defecto, hotel
            accommodationDetail = hotelRepository.findHotelDetailById(code);
            if (accommodationDetail == null) {
                return ResponseVO.<AccommodationDetailVO>builder()
                        .response(null)
                        .messages(new MessageResponseVO("No se encontró el alojamiento", 404, LocalDateTime.now()))
                        .build();
            }
            images = hotelRepository.findAllHotelImages(code);
            availableRooms = hotelRepository.findAvailableRoomsByHotel(
                    code, checkIn, checkOut, effectiveRooms, effectiveAdults, effectiveChildren);
            accommodationDetail.setAccommodationType("hotel");
        }

        accommodationDetail.setImages(images);
        accommodationDetail.setAvailableRooms(availableRooms);

        return ResponseVO.<AccommodationDetailVO>builder()
                .response(new DataVO<>(accommodationDetail))
                .messages(new MessageResponseVO("Detalles del alojamiento obtenidos correctamente", 200, LocalDateTime.now()))
                .build();
    }

    public List<LocalDate> getAvailableDates(List<Long> roomConfigIds, LocalDate startDate, LocalDate endDate) {
        List<java.sql.Date> dates = roomBookingRepository.findAvailableDates(
                roomConfigIds,
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate)
        );
        return dates.stream().map(java.sql.Date::toLocalDate).toList();
    }

    public List<LocalDate> getAvailableDatesApartment(List<Long> roomConfigIds, LocalDate startDate, LocalDate endDate) {
        List<java.sql.Date> dates = roomBookingRepository.findAvailableDatesAp(
                roomConfigIds,
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate)
        );
        return dates.stream().map(java.sql.Date::toLocalDate).toList();
    }

    public ResponseVO<Void> realizarReserva(RealizarReservaRequest request, String authHeader, LocalDate startDate, LocalDate endDate) {
        if (request.getRooms() == null || request.getRooms().isEmpty()) {
            return ResponseVO.<Void>builder()
                    .messages(new MessageResponseVO("No se han seleccionado habitaciones", 400, LocalDateTime.now()))
                    .build();
        }
        if (request.getAccommodationCode() == null || request.getAccommodationType() == null) {
            return ResponseVO.<Void>builder()
                    .messages(new MessageResponseVO("Datos de alojamiento incompletos", 400, LocalDateTime.now()))
                    .build();
        }
        
        User user = jwtService.getUserFromToken(authHeader);
        if (user == null) {
            return ResponseVO.<Void>builder()
                    .messages(new MessageResponseVO("Usuario no autenticado", 401, LocalDateTime.now()))
                    .build();
        }

        try {
            if ("apartment".equalsIgnoreCase(request.getAccommodationType())) {
                return processApartmentReservation(request, user, startDate, endDate);
            } else {
                return processHotelReservation(request, user, startDate, endDate);
            }
        } catch (Exception e) {
            return ResponseVO.<Void>builder()
                    .messages(new MessageResponseVO("Error al realizar la reserva: " + e.getMessage(), 500, LocalDateTime.now()))
                    .build();
        }
    }

    private ResponseVO<Void> processHotelReservation(RealizarReservaRequest request, User user, LocalDate startDate, LocalDate endDate) {
        List<RoomBooking> bookings = new ArrayList<>();
        double totalAmount = 0.0;
        
        // Obtener información del hotel
        Hotel hotel = hotelRepository.findByCode(request.getAccommodationCode())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        for (RoomSelectionRequest selection : request.getRooms()) {
            // Obtener configuración de habitación y precio
            RoomConfigurationHotel roomConfig = roomConfigurationHotelRepository.findById(selection.getRoomConfigId())
                    .orElseThrow(() -> new RuntimeException("Configuración de habitación no encontrada"));
            
            List<Room> allRooms = roomRepository.findAllRoomsByRoomConfigurationId(selection.getRoomConfigId());
            List<Room> freeRooms = allRooms.stream().limit(selection.getQty()).toList();
            
            if (freeRooms.size() < selection.getQty()) {
                return ResponseVO.<Void>builder()
                    .messages(new MessageResponseVO("No hay suficientes habitaciones libres para la configuración " + selection.getRoomConfigId(), 400, LocalDateTime.now()))
                    .build();
            }

            for (int i = 0; i < selection.getQty(); i++) {
                Room room = freeRooms.get(i);
                RoomBooking booking = new RoomBooking();
                booking.setRoom(room);
                booking.setUser(user);
                booking.setStartDate(startDate);
                booking.setEndDate(endDate);
                booking.setStatus(StatusRoomBooking.CONFIRMED);
                booking = roomBookingRepository.save(booking);
                bookings.add(booking);
                
                // Calcular días de estancia y precio total
                long days = startDate.until(endDate).getDays();
                totalAmount += roomConfig.getPrice() * days;
            }
        }

        // Crear orden del hotel
        OrderHotel orderHotel = new OrderHotel();
        orderHotel.setCode(userService.generateShortUuid().substring(0, 10));
        orderHotel.setAmount((float) totalAmount);
        orderHotel.setDiscount(0.0f);
        orderHotel.setStatus(StatusOrder.Paid);
        
        // Tomar la primera reserva para asociar a la orden (podrías modificar la estructura según necesites)
        orderHotel.setRoomBooking(bookings.get(0));
        
        // Generar factura
        byte[] invoicePdf = generateHotelInvoice(orderHotel, user, hotel, bookings, startDate, endDate);
        orderHotel.setBill(invoicePdf);
        
        orderHotelRepository.save(orderHotel);

        // Enviar email con factura
        sendInvoiceEmail(user, hotel.getName(), "hotel", invoicePdf, orderHotel.getCode());

        return ResponseVO.<Void>builder()
                .messages(new MessageResponseVO("Reserva de hotel realizada con éxito", 200, LocalDateTime.now()))
                .build();
    }

    private ResponseVO<Void> processApartmentReservation(RealizarReservaRequest request, User user, LocalDate startDate, LocalDate endDate) {
        List<RoomAparmentBooking> bookings = new ArrayList<>();
        double totalAmount = 0.0;
        
        // Obtener información del apartamento
        Apartment apartment = apartmentRepository.findByCode(request.getAccommodationCode())
                .orElseThrow(() -> new RuntimeException("Apartamento no encontrado"));

        for (RoomSelectionRequest selection : request.getRooms()) {
            // Obtener configuración de habitación y precio
            RoomConfigurationApartment roomConfig = roomConfigurationApartmentRepository.findById(selection.getRoomConfigId())
                    .orElseThrow(() -> new RuntimeException("Configuración de habitación no encontrada"));
            
            List<RoomApartment> allRooms = roomApartmentRepository.findAllRoomsByRoomConfigurationId(selection.getRoomConfigId());
            List<RoomApartment> freeRooms = allRooms.stream().limit(selection.getQty()).toList();
            
            if (freeRooms.size() < selection.getQty()) {
                return ResponseVO.<Void>builder()
                    .messages(new MessageResponseVO("No hay suficientes habitaciones/apartamentos libres para la configuración " + selection.getRoomConfigId(), 400, LocalDateTime.now()))
                    .build();
            }

            for (int i = 0; i < selection.getQty(); i++) {
                RoomApartment room = freeRooms.get(i);
                RoomAparmentBooking booking = new RoomAparmentBooking();
                booking.setRoom(room);
                booking.setUser(user);
                booking.setStartDate(startDate);
                booking.setEndDate(endDate);
                booking.setStatus(StatusRoomBooking.CONFIRMED);
                booking = roomAparmentBookingRepository.save(booking);
                bookings.add(booking);
                
                // Calcular días de estancia y precio total
                long days = startDate.until(endDate).getDays();
                totalAmount += roomConfig.getPrice() * days;
            }
        }

        // Crear orden del apartamento
        OrderApartment orderApartment = new OrderApartment();
        orderApartment.setCode(userService.generateShortUuid().substring(0, 10));
        orderApartment.setAmount((float) totalAmount);
        orderApartment.setDiscount(0.0f);
        orderApartment.setStatus(StatusOrder.Paid);
        
        // Tomar la primera reserva para asociar a la orden
        orderApartment.setRoomBooking(bookings.get(0));
        
        // Generar factura
        byte[] invoicePdf = generateApartmentInvoice(orderApartment, user, apartment, bookings, startDate, endDate);
        orderApartment.setBill(invoicePdf);
        
        orderApartmentRepository.save(orderApartment);

        // Enviar email con factura
        sendInvoiceEmail(user, apartment.getName(), "apartment", invoicePdf, orderApartment.getCode());

        return ResponseVO.<Void>builder()
                .messages(new MessageResponseVO("Reserva de apartamento realizada con éxito", 200, LocalDateTime.now()))
                .build();
    }

    private byte[] generateHotelInvoice(OrderHotel order, User user, Hotel hotel, List<RoomBooking> bookings, LocalDate startDate, LocalDate endDate) {
        Context context = new Context();
        context.setVariable("orderCode", order.getCode());
        context.setVariable("userName", user.getName() + " " + user.getLastName());
        context.setVariable("userEmail", user.getEmail());
        context.setVariable("hotelName", hotel.getName());
        context.setVariable("hotelAddress", hotel.getAddress());
        context.setVariable("checkIn", startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("checkOut", endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("totalAmount", order.getAmount());
        context.setVariable("discount", order.getDiscount());
        context.setVariable("finalAmount", order.getAmount() - order.getDiscount());
        context.setVariable("bookingDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("accommodationType", "Hotel");
        
        // Agregar detalles de las habitaciones
        List<String> roomDetails = bookings.stream()
                .map(booking -> "Habitación " + booking.getRoom().getRoomNumber() + 
                               " - Tipo: " + booking.getRoom().getRoomConfiguration().getRoomConfiguration().getType())
                .collect(Collectors.toList());
        context.setVariable("roomDetails", roomDetails);

        String htmlContent = templateEngine.process("invoice-template-acc", context);

        return convertHtmlToPdf(htmlContent);
    }

    private byte[] generateApartmentInvoice(OrderApartment order, User user, Apartment apartment, List<RoomAparmentBooking> bookings, LocalDate startDate, LocalDate endDate) {
        Context context = new Context();
        context.setVariable("orderCode", order.getCode());
        context.setVariable("userName", user.getName() + " " + user.getLastName());
        context.setVariable("userEmail", user.getEmail());
        context.setVariable("hotelName", apartment.getName());
        context.setVariable("hotelAddress", apartment.getAddress());
        context.setVariable("checkIn", startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("checkOut", endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("totalAmount", order.getAmount());
        context.setVariable("discount", order.getDiscount());
        context.setVariable("finalAmount", order.getAmount() - order.getDiscount());
        context.setVariable("bookingDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("accommodationType", "Apartamento");
        
        // Agregar detalles de las habitaciones
        List<String> roomDetails = bookings.stream()
                .map(booking -> "Habitación " + booking.getRoom().getRoomNumber() + 
                               " - Tipo: " + booking.getRoom().getRoomConfiguration().getRoomConfiguration().getType())
                .collect(Collectors.toList());
        context.setVariable("roomDetails", roomDetails);

        String htmlContent = templateEngine.process("invoice-template-acc", context);

        return convertHtmlToPdf(htmlContent);
    }

    private byte[] convertHtmlToPdf(String htmlContent) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            System.err.println("Error al convertir HTML a PDF: " + e.getMessage());
            // En caso de error, devuelve el HTML como bytes como fallback
            return htmlContent.getBytes();
        }
    }

    private void sendInvoiceEmail(User user, String accommodationName, String type, byte[] invoice, String orderCode) {
        try {
            String subject = "✈️ Confirmación de Reserva SkyStay - " + accommodationName;
            String htmlMessage = buildEmailMessage(user, accommodationName, type, orderCode);

            emailService.sendEmailWithAttachment(
                    user.getEmail(),
                    subject,
                    htmlMessage,
                    invoice,
                    "SkyStay_Factura_" + orderCode + ".pdf"
            );
        } catch (Exception e) {
            // Log error but don't fail the reservation
            System.err.println("Error enviando email: " + e.getMessage());
        }
    }

    private String buildEmailMessage(User user, String accommodationName, String type, String orderCode) {
        return String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body { font-family: Arial, sans-serif; color: #333; line-height: 1.6; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background: linear-gradient(135deg, #7cafc4, #437c97); color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }
                .content { background: #f9fafb; padding: 30px; border: 1px solid #e1e5e9; }
                .highlight { background: #e8f4f8; padding: 15px; border-left: 4px solid #7cafc4; margin: 20px 0; border-radius: 4px; }
                .footer { background: #2d3f4c; color: #bbb; padding: 20px; text-align: center; border-radius: 0 0 8px 8px; font-size: 12px; }
                .button { background: #7cafc4; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block; margin: 10px 0; }
                .info-row { margin: 10px 0; }
                .label { font-weight: bold; color: #437c97; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>🏨 SkyStay</h1>
                    <h2>¡Reserva Confirmada!</h2>
                </div>
                
                <div class="content">
                    <p>Estimado/a <strong>%s</strong>,</p>
                    
                    <p>Nos complace confirmar que su reserva ha sido procesada exitosamente. ¡Estamos emocionados de ser parte de su próxima aventura!</p>
                    
                    <div class="highlight">
                        <h3>📋 Detalles de su Reserva</h3>
                        <div class="info-row">
                            <span class="label">🏢 Alojamiento:</span> %s
                        </div>
                        <div class="info-row">
                            <span class="label">🏷️ Tipo:</span> %s
                        </div>
                        <div class="info-row">
                            <span class="label">🔑 Código de Reserva:</span> <strong>%s</strong>
                        </div>
                    </div>
                    
                    <p><strong>📎 Documentación Adjunta:</strong><br>
                    Encontrará su factura detallada adjunta a este correo en formato PDF. Le recomendamos guardarla para sus registros.</p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <p>¿Necesita hacer cambios en su reserva?</p>
                        <a href="#" class="button">Gestionar Reserva</a>
                    </div>
                    
                    <p><strong>📞 ¿Necesita Ayuda?</strong><br>
                    Nuestro equipo de atención al cliente está disponible 24/7 para asistirle:</p>
                    
                    <ul>
                        <li>📧 Email: support@skystay.com</li>
                        <li>📱 WhatsApp: +34 900 123 456</li>
                        <li>☎️ Teléfono: +34 900 654 321</li>
                    </ul>
                    
                    <p>Gracias por elegir <strong>SkyStay</strong>. ¡Esperamos que disfrute de una experiencia inolvidable!</p>
                    
                    <p>Con cariño,<br>
                    <strong>El equipo de SkyStay</strong> ✈️</p>
                </div>
                
                <div class="footer">
                    <p>Este correo fue enviado automáticamente desde SkyStay.<br>
                    © 2025 SkyStay. Todos los derechos reservados.</p>
                    <p style="margin-top: 10px;">
                        <a href="#" style="color: #7cafc4;">Política de Privacidad</a> | 
                        <a href="#" style="color: #7cafc4;">Términos de Servicio</a> | 
                        <a href="#" style="color: #7cafc4;">Cancelar Suscripción</a>
                    </p>
                </div>
            </div>
        </body>
        </html>
        """,
                user.getName(),
                accommodationName,
                type,
                orderCode
        );
    }
}
