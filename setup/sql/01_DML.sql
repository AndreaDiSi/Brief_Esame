
INSERT INTO host (is_superhost, email, host_name, surname, host_address) VALUES
(TRUE, 'mario.rossi@example.com', 'Mario', 'Rossi', 'Via Roma 10, Milano'),
(FALSE, 'laura.bianchi@example.com', 'Laura', 'Bianchi', 'Corso Italia 25, Roma'),
(TRUE, 'giuseppe.verdi@example.com', 'Giuseppe', 'Verdi', 'Piazza Duomo 5, Firenze'),
(FALSE, 'anna.neri@example.com', 'Anna', 'Neri', 'Via Garibaldi 30, Napoli'),
(TRUE, 'luca.ferrari@example.com', 'Luca', 'Ferrari', 'Viale Europa 15, Bologna');


INSERT INTO tenant (email, tenant_name, surname, tenant_address) VALUES
('paolo.conti@example.com', 'Paolo', 'Conti', 'Via Dante 20, Torino'),
('sara.marino@example.com', 'Sara', 'Marino', 'Corso Venezia 40, Venezia'),
('marco.russo@example.com', 'Marco', 'Russo', 'Via Mazzini 12, Genova'),
('giulia.costa@example.com', 'Giulia', 'Costa', 'Piazza San Marco 8, Verona'),
('alessandro.greco@example.com', 'Alessandro', 'Greco', 'Via Toledo 50, Palermo');


INSERT INTO accomodation (accomodation_name, n_rooms, accomodation_address, id_host, n_bed_places, floor, starter_date, end_date, price) VALUES
('Casa Vittoria', 3, 'Via Montenapoleone 10, Milano', 1, 6, 2, '2024-01-01', '2024-12-31', 150),
('Appartamento Roma', 2, 'Via del Corso 100, Roma', 2, 4, 1, '2024-01-01', '2024-12-31', 120),
('Villa Toscana', 5, 'Via Tornabuoni 15, Firenze', 3, 10, 0, '2024-03-01', '2024-10-31', 300),
('Loft Napoli', 1, 'Via Chiaia 45, Napoli', 4, 2, 3, '2024-01-15', '2024-12-15', 80),
('Attico Bologna', 4, 'Via Rizzoli 20, Bologna', 5, 8, 5, '2024-02-01', '2024-11-30', 200);

INSERT INTO reservation (reservation_start_date, reservation_end_date, id_tenant, id_accomodation) VALUES
('2024-06-01', '2024-06-07', 1, 1),
('2024-07-10', '2024-07-20', 2, 2),
('2024-08-05', '2024-08-15', 3, 3),
('2024-09-01', '2024-09-05', 4, 4),
('2024-10-12', '2024-10-19', 5, 5),
('2024-06-15', '2024-06-22', 1, 3),
('2024-07-25', '2024-08-01', 2, 1);


INSERT INTO feedback (title, text_feedback, points, id_reservation) VALUES
('Ottima esperienza', 'Appartamento pulito e host disponibile. Consigliatissimo!', 5, 1),
('Buon soggiorno', 'Posizione centrale, anche se un po rumoroso di notte.', 4, 2),
('Fantastico!', 'Villa bellissima con giardino meraviglioso. Esperienza indimenticabile.', 5, 3),
('Nella norma', 'Appartamento piccolo ma funzionale. Prezzo giusto.', 3, 4),
('Eccellente', 'Attico lussuoso con vista mozzafiato. Tornerò sicuramente!', 5, 5),
(NULL, NULL, 4, 6),
('Molto bene', 'Host gentilissimo e casa accogliente.', 4, 7);