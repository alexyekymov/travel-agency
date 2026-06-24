insert into tour (id, title, description, price, arrival_date, eviction_date,
                  tour_type, hotel_type, transfer_type, hot, created_at, created_by)
values ('11111111-1111-1111-1111-111111111111', 'Paris City Break', 'd', 1000.00, '2026-07-01', '2026-07-10',
        'LEISURE', 'FIVE_STARS', 'PLANE', false, '2026-01-01 10:00:00', null),
       ('22222222-2222-2222-2222-222222222222', 'Aegean Cruise', 'd', 500.00, '2026-07-01', '2026-07-10',
        'SPORTS', 'THREE_STARS', 'BUS', true, '2026-01-02 10:00:00', null),
       ('33333333-3333-3333-3333-333333333333', 'Tuscan Vineyards', 'd', 1500.00, '2026-07-01', '2026-07-10',
        'LEISURE', 'FOUR_STARS', 'TRAIN', false, '2026-01-03 10:00:00', null),
       ('44444444-4444-4444-4444-444444444444', 'Sahara Expedition', 'd', 2000.00, '2026-07-01', '2026-07-10',
        'WINE', 'FIVE_STARS', 'SHIP', true, '2026-01-04 10:00:00', null);
