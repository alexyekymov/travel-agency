INSERT INTO app_user
(id, first_name, last_name, email, password, phone_number, account_status)
VALUES ('019ef133-47c6-78fb-b57a-474fc31850d6', 'Olha', 'Kovalenko', 'olha.kovalenko@example.com','{bcrypt}$2a$10$4flWgjeRgVvFFSmnsF1EjuaJIwPNvjtVyUeLR09o0.iN9wGn6yMQC', '+380501112201', true),
       ('019ef133-8ad7-7de9-8953-ff95a41361c1','Andriy', 'Shevchenko', 'andriy.shevchenko@example.com','{bcrypt}$2a$10$4flWgjeRgVvFFSmnsF1EjuaJIwPNvjtVyUeLR09o0.iN9wGn6yMQC', '+380501112202', true),
       -- MANAGERS
       ('019ef133-be7d-7d1d-ba1a-4618d3170b42', 'Anna', 'Petrenko', 'anna.petrenko@example.com', '{bcrypt}$2a$10$4flWgjeRgVvFFSmnsF1EjuaJIwPNvjtVyUeLR09o0.iN9wGn6yMQC', '+380501112217', true),
       ('019ef133-cf4e-7f79-a6a4-e2ed0dc488bf', 'Ihor', 'Moroz', 'ihor.moroz@example.com', '{bcrypt}$2a$10$4flWgjeRgVvFFSmnsF1EjuaJIwPNvjtVyUeLR09o0.iN9wGn6yMQC', '+380501112218', true),
       -- ADMIN
       ('019ef134-0c2f-7c88-9fba-4baaf6c0a271', 'Viktoriia', 'Symonenko', 'viktoriia.symonenko@example.com', '{bcrypt}$2a$10$4flWgjeRgVvFFSmnsF1EjuaJIwPNvjtVyUeLR09o0.iN9wGn6yMQC', '+380501112219', true);
