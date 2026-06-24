insert into app_user (id, first_name, last_name, email, password, account_status)
values ('aaaaaaaa-0000-0000-0000-000000000001', 'Ann', 'A', 'ann@example.com', 'pw', true),
       ('bbbbbbbb-0000-0000-0000-000000000002', 'Bob', 'B', 'bob@example.com', 'pw', true);

-- 1,2 - Ann; 3- Bob
insert into orders (id, user_id, total_price, status, created_at)
values ('00000000-0000-0000-0000-0000000000a1', 'aaaaaaaa-0000-0000-0000-000000000001', 100.00, 'PENDING', '2026-02-01 10:00:00'),
       ('00000000-0000-0000-0000-0000000000a2', 'aaaaaaaa-0000-0000-0000-000000000001', 200.00, 'PAID', '2026-02-05 10:00:00'),
       ('00000000-0000-0000-0000-0000000000b1', 'bbbbbbbb-0000-0000-0000-000000000002', 300.00, 'PENDING', '2026-02-03 10:00:00');
