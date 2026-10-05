package com.campuscircular.config;

import com.campuscircular.model.Exchange;
import com.campuscircular.model.Resource;
import com.campuscircular.model.User;
import com.campuscircular.repository.ExchangeRepository;
import com.campuscircular.repository.ResourceRepository;
import com.campuscircular.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Seeds the database on first startup with mock users, resources, and exchanges
 * matching the original frontend data files (users.js, resources.js, exchanges.js).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final ExchangeRepository exchangeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded – skipping.");
            return;
        }

        log.info("Seeding database with initial data...");

        // ── USERS ────────────────────────────────────────────────────────────────
        String defaultPassword = passwordEncoder.encode("password123");

        User u1 = save(User.builder().id("u1").name("Arjun Mehta").email("arjun.mehta@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Computer Science").year(3).verified(true)
                .trustScore(94).onTimeReturnRate(97).successfulExchanges(28).conditionAccuracy(96).responseRate(92)
                .phone("+91 98765 43210").badges(List.of("Top Lender","Quick Responder","Trusted Veteran")).build());

        User u2 = save(User.builder().id("u2").name("Priya Sharma").email("priya.sharma@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Mechanical Engineering").year(4).verified(true)
                .trustScore(91).onTimeReturnRate(95).successfulExchanges(22).conditionAccuracy(93).responseRate(88)
                .phone("+91 87654 32109").badges(List.of("Reliable Lender","Gear Specialist")).build());

        User u3 = save(User.builder().id("u3").name("Rohan Desai").email("rohan.desai@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Electrical Engineering").year(2).verified(true)
                .trustScore(87).onTimeReturnRate(90).successfulExchanges(15).conditionAccuracy(89).responseRate(95)
                .phone("+91 76543 21098").badges(List.of("Quick Responder","Rising Star")).build());

        User u4 = save(User.builder().id("u4").name("Sneha Kulkarni").email("sneha.kulkarni@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Architecture").year(3).verified(true)
                .trustScore(92).onTimeReturnRate(98).successfulExchanges(19).conditionAccuracy(95).responseRate(90)
                .phone("+91 65432 10987").badges(List.of("Perfect Returns","Trusted Veteran")).build());

        User u5 = save(User.builder().id("u5").name("Vikram Singh").email("vikram.singh@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Physics").year(4).verified(true)
                .trustScore(85).onTimeReturnRate(88).successfulExchanges(12).conditionAccuracy(90).responseRate(82)
                .unresolvedDisputes(1).phone("+91 54321 09876").badges(List.of("Gear Specialist")).build());

        User u6 = save(User.builder().id("u6").name("Ananya Iyer").email("ananya.iyer@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Media Studies").year(2).verified(true)
                .trustScore(89).onTimeReturnRate(93).successfulExchanges(17).conditionAccuracy(91).responseRate(96)
                .phone("+91 43210 98765").badges(List.of("Quick Responder","Content Creator")).build());

        User u7 = save(User.builder().id("u7").name("Kabir Patel").email("kabir.patel@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Civil Engineering").year(3).verified(false)
                .trustScore(78).onTimeReturnRate(85).successfulExchanges(8).conditionAccuracy(82).responseRate(75)
                .phone("+91 32109 87654").badges(List.of("Rising Star")).build());

        User u8 = save(User.builder().id("u8").name("Divya Nair").email("divya.nair@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Biotechnology").year(4).verified(true)
                .trustScore(96).onTimeReturnRate(99).successfulExchanges(35).conditionAccuracy(97).responseRate(94)
                .phone("+91 21098 76543").badges(List.of("Top Lender","Perfect Returns","Trusted Veteran","Community Champion")).build());

        User u9 = save(User.builder().id("u9").name("Aditya Joshi").email("aditya.joshi@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Mathematics").year(1).verified(false)
                .trustScore(72).onTimeReturnRate(80).successfulExchanges(3).conditionAccuracy(78).responseRate(70)
                .phone("+91 10987 65432").badges(List.of("Newcomer")).build());

        User u10 = save(User.builder().id("u10").name("Meera Reddy").email("meera.reddy@campus.edu").password(defaultPassword)
                .role(User.Role.STUDENT).department("Design").year(3).verified(true)
                .trustScore(90).onTimeReturnRate(94).successfulExchanges(20).conditionAccuracy(92).responseRate(91)
                .phone("+91 09876 54321").badges(List.of("Reliable Lender","Creative Kit Expert")).build());

        // Admin user
        save(User.builder().id("admin1").name("System Admin").email("admin@campus.edu")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN).department("Administration").verified(true)
                .trustScore(100).badges(List.of("Platform Admin")).build());

        // ── RESOURCES ────────────────────────────────────────────────────────────
        Resource r1 = saveResource(Resource.builder().id("r1").name("Canon EOS 200D II").category("camera")
                .condition("Excellent").available(true).distance(0.28).rating(4.8).pricePerDay(350).deposit(2000)
                .description("Lightweight DSLR with 24.1MP sensor, perfect for reels and short films.")
                .accessories(List.of("18-55mm Kit Lens","Battery Charger","Camera Strap","16GB SD Card"))
                .locationLabel("Hostel 7, Room 302").locationArea("Near Hostel 7")
                .locationLat(19.1334).locationLng(72.9133).exchangeCount(14).owner(u1).build());

        Resource r2 = saveResource(Resource.builder().id("r2").name("Sony Alpha 6100").category("camera")
                .condition("Good").available(true).distance(0.45).rating(4.6).pricePerDay(500).deposit(3000)
                .description("Mirrorless camera with outstanding autofocus.")
                .accessories(List.of("Battery","USB-C Cable","Body Cap"))
                .locationLabel("Media Lab, 1st Floor").locationArea("Near Media Studies Block")
                .locationLat(19.1340).locationLng(72.9148).exchangeCount(9).damageReportCount(1).owner(u6).build());

        saveResource(Resource.builder().id("r3").name("Manfrotto Compact Tripod").category("tripod")
                .condition("Excellent").available(true).distance(0.28).rating(4.9).pricePerDay(80).deposit(500)
                .description("Lightweight aluminium tripod, extends to 165cm.")
                .accessories(List.of("Carry Bag","Phone Mount Adapter"))
                .locationLabel("Hostel 7, Room 302").locationArea("Near Hostel 7")
                .locationLat(19.1334).locationLng(72.9133).exchangeCount(22).owner(u1).build());

        saveResource(Resource.builder().id("r4").name("Boya BY-M1 Lavalier Mic").category("microphone")
                .condition("Good").available(true).distance(0.62).rating(4.5).pricePerDay(60).deposit(300)
                .description("Clip-on lavalier microphone for clear voice recording.")
                .accessories(List.of("3.5mm Adapter","Windscreen","Carry Pouch"))
                .locationLabel("Academic Block B, Room 105").locationArea("Near Academic Block B")
                .locationLat(19.1328).locationLng(72.9155).exchangeCount(18).owner(u3).build());

        saveResource(Resource.builder().id("r5").name("Godox SL-60W LED Light").category("lighting")
                .condition("Excellent").available(false).distance(0.90).rating(4.7).pricePerDay(200).deposit(1500)
                .description("60W continuous LED video light with Bowens mount.")
                .accessories(List.of("Power Cable","Standard Reflector","Light Stand"))
                .locationLabel("Design Studio, Ground Floor").locationArea("Near Design Block")
                .locationLat(19.1352).locationLng(72.9140).exchangeCount(7).owner(u10).build());

        saveResource(Resource.builder().id("r6").name("MacBook Air M2").category("laptop")
                .condition("Excellent").available(true).distance(0.35).rating(4.9).pricePerDay(600).deposit(5000)
                .description("2023 MacBook Air with M2 chip, 8GB RAM, 256GB SSD.")
                .accessories(List.of("MagSafe Charger","USB-C Hub"))
                .locationLabel("Library Building, 2nd Floor").locationArea("Near Central Library")
                .locationLat(19.1338).locationLng(72.9128).exchangeCount(6).owner(u8).build());

        Resource r7 = saveResource(Resource.builder().id("r7").name("Casio fx-991EX Scientific Calculator").category("calculator")
                .condition("Good").available(true).distance(0.15).rating(4.4).pricePerDay(25).deposit(200)
                .description("Advanced scientific calculator with 552 functions.")
                .accessories(List.of("Protective Case"))
                .locationLabel("Hostel 3, Room 118").locationArea("Near Hostel 3")
                .locationLat(19.1330).locationLng(72.9125).exchangeCount(30).owner(u5).build());

        saveResource(Resource.builder().id("r8").name("Engineering Mechanics by Timoshenko").category("textbook")
                .condition("Fair").available(true).distance(0.50).rating(4.2).pricePerDay(15).deposit(150)
                .description("5th edition with highlighting. All pages intact.")
                .locationLabel("Hostel 5, Room 210").locationArea("Near Hostel 5")
                .locationLat(19.1342).locationLng(72.9138).exchangeCount(12).owner(u7).build());

        saveResource(Resource.builder().id("r9").name("Epson EB-X51 Projector").category("projector")
                .condition("Good").available(true).distance(0.72).rating(4.6).pricePerDay(400).deposit(3000)
                .description("3800 lumens XGA projector with HDMI input.")
                .accessories(List.of("HDMI Cable","VGA Cable","Power Cable","Remote Control"))
                .locationLabel("Academic Block A, Room 301").locationArea("Near Academic Block A")
                .locationLat(19.1345).locationLng(72.9150).exchangeCount(8).owner(u4).build());

        Resource r10 = saveResource(Resource.builder().id("r10").name("Kookaburra Cricket Bat").category("sports")
                .condition("Good").available(true).distance(0.40).rating(4.3).pricePerDay(100).deposit(800)
                .description("English willow bat, well-knocked in.")
                .accessories(List.of("Bat Cover"))
                .locationLabel("Sports Complex, Equipment Room").locationArea("Near Sports Complex")
                .locationLat(19.1320).locationLng(72.9160).exchangeCount(15).damageReportCount(1).owner(u2).build());

        saveResource(Resource.builder().id("r11").name("Arduino Mega Starter Kit").category("tools")
                .condition("Excellent").available(true).distance(0.55).rating(4.7).pricePerDay(120).deposit(1000)
                .description("Complete Arduino Mega 2560 kit.")
                .accessories(List.of("USB Cable","Breadboard","65 Jumper Wires","10 Sensors","Servo Motor"))
                .locationLabel("Electronics Lab, Ground Floor").locationArea("Near Electronics Lab")
                .locationLat(19.1336).locationLng(72.9145).exchangeCount(11).owner(u3).build());

        saveResource(Resource.builder().id("r12").name("Rode VideoMicro Shotgun Mic").category("microphone")
                .condition("Excellent").available(true).distance(0.45).rating(4.8).pricePerDay(120).deposit(800)
                .description("Compact on-camera shotgun microphone.")
                .accessories(List.of("Shock Mount","Windshield","3.5mm TRS Cable"))
                .locationLabel("Media Lab, 1st Floor").locationArea("Near Media Studies Block")
                .locationLat(19.1340).locationLng(72.9148).exchangeCount(13).owner(u6).build());

        saveResource(Resource.builder().id("r13").name("HP Pavilion 15 Laptop").category("laptop")
                .condition("Good").available(false).distance(0.30).rating(4.1).pricePerDay(400).deposit(3500)
                .description("i5 12th Gen, 8GB RAM, 512GB SSD.")
                .accessories(List.of("Charger","Laptop Sleeve"))
                .locationLabel("Hostel 9, Room 415").locationArea("Near Hostel 9")
                .locationLat(19.1348).locationLng(72.9130).exchangeCount(5).owner(u9).build());

        saveResource(Resource.builder().id("r14").name("Badminton Racket Pair (Yonex)").category("sports")
                .condition("Good").available(true).distance(0.38).rating(4.5).pricePerDay(60).deposit(400)
                .description("Pair of Yonex Nanoray rackets with fresh strings.")
                .accessories(List.of("2 Rackets","3 Shuttlecocks","Carry Bag"))
                .locationLabel("Sports Complex, Equipment Room").locationArea("Near Sports Complex")
                .locationLat(19.1320).locationLng(72.9160).exchangeCount(20).owner(u2).build());

        saveResource(Resource.builder().id("r15").name("Digital Multimeter (Fluke 117)").category("tools")
                .condition("Excellent").available(true).distance(0.55).rating(4.9).pricePerDay(80).deposit(600)
                .description("True-RMS multimeter ideal for electrical lab experiments.")
                .accessories(List.of("Test Leads","Carry Case","Battery"))
                .locationLabel("Electronics Lab, Ground Floor").locationArea("Near Electronics Lab")
                .locationLat(19.1336).locationLng(72.9145).exchangeCount(16).owner(u3).build());

        // ── SAMPLE EXCHANGES ─────────────────────────────────────────────────────
        Instant now = Instant.now();

        exchangeRepository.save(Exchange.builder()
                .id("ex1").resource(r1).borrower(u3).lender(u1)
                .status(Exchange.ExchangeStatus.rated).duration(2)
                .borrowingCharge(700).platformFee(70).deposit(2000).totalAmount(2770)
                .refundAmount(2000).lateFee(0).damageDeduction(0).rating(5)
                .requestedAt(now.minus(30, ChronoUnit.DAYS))
                .acceptedAt(now.minus(30, ChronoUnit.DAYS).plus(90, ChronoUnit.MINUTES))
                .handoverAt(now.minus(30, ChronoUnit.DAYS).plus(4, ChronoUnit.HOURS))
                .returnDue(now.minus(28, ChronoUnit.DAYS))
                .returnedAt(now.minus(28, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .settledAt(now.minus(28, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS))
                .ratedAt(now.minus(28, ChronoUnit.DAYS).plus(5, ChronoUnit.HOURS))
                .conditionBefore("{\"body\":\"Good\",\"lens\":\"Clean\",\"battery\":\"Full\"}")
                .conditionAfter("{\"body\":\"Good\",\"lens\":\"Clean\",\"battery\":\"60%\"}")
                .build());

        exchangeRepository.save(Exchange.builder()
                .id("ex2").resource(r7).borrower(u9).lender(u5)
                .status(Exchange.ExchangeStatus.rated).duration(1)
                .borrowingCharge(25).platformFee(3).deposit(200).totalAmount(228)
                .refundAmount(200).lateFee(0).damageDeduction(0).rating(4)
                .requestedAt(now.minus(20, ChronoUnit.DAYS))
                .acceptedAt(now.minus(20, ChronoUnit.DAYS).plus(45, ChronoUnit.MINUTES))
                .ratedAt(now.minus(19, ChronoUnit.DAYS))
                .build());

        exchangeRepository.save(Exchange.builder()
                .id("ex3").resource(r10).borrower(u7).lender(u2)
                .status(Exchange.ExchangeStatus.rated).duration(2)
                .borrowingCharge(200).platformFee(20).deposit(800).totalAmount(1020)
                .refundAmount(650).lateFee(100).damageDeduction(50).rating(3)
                .requestedAt(now.minus(15, ChronoUnit.DAYS))
                .acceptedAt(now.minus(15, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .ratedAt(now.minus(13, ChronoUnit.DAYS))
                .build());

        log.info("Database seeding complete.");
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    private Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }
}
