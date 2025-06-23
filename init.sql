create table if not exists items
(
    id          serial primary key not null,
    title       text               not null,
    price       numeric            not null,
    description varchar            not null,
    img_path    varchar            not null
);

create table if not exists orders
(
    id        serial primary key not null,
    status    varchar            not null,
    completed timestamp          not null default now(),
    created   timestamp          not null default now()
);

create table if not exists orders_items
(
    id       serial primary key not null,
    order_id integer            not null,
    constraint orders_items_orders_fkey foreign key (order_id)
        references orders (id) match simple
        on update cascade on delete cascade,
    item_id  integer            not null,
    constraint orders_items_items_fkey foreign key (item_id)
        references items (id) match simple
        on update cascade on delete cascade,
    count    smallint           not null default 0
);

create table if not exists accounts
(
    id      serial primary key not null,
    user_id integer            not null,
    balance float              not null
);


insert into items (title, description, img_path, price)
values ('Сосиски1',
        'Куриные',
        'https://img01.kupiprodai.ru/082015/1439552248188.jpg',
        10);

insert into items (title, description, img_path, price)
values ('Сосиски2',
        'Куриные/Мясные',
        'https://avatars.mds.yandex.net/get-mpic/6175789/img_id6989388605594939163.jpeg/orig',
        11);

insert into items (title, description, img_path, price)
values ('Сосиски3',
        'Куриные',
        'https://www.kolbasa-opt.ru/wa-data/public/products/97/85/28597/images/1878/1878.750x0.jpg',
        12);

insert into items (title, description, img_path, price)
values ('Сосиски4',
        'Куриные/Мясные',
        'https://avatars.mds.yandex.net/get-eda/3506707/c15b65655a4d34a5bdc49f58b0069698/orig',
        13);

insert into items (title, description, img_path, price)
values ('Сосиски5',
        'Куриные',
        'https://i2015.otzovik.com/2015/10/27/2543850/img/20967206.jpg',
        14);

insert into items (title, description, img_path, price)
values ('Сосиски6',
        'Куриные/Мясные',
        'https://biomiks.com/upload/iblock/7a1/7a14792644a51ad040b30df9e54b9822.jpg',
        15);

insert into items (title, description, img_path, price)
values ('Сосиски7',
        'Мясные',
        'https://mig.pics/x/uploads/posts/2022-09/1663817933_23-mykaleidoscope-ru-p-sosiski-v-vakuumnoi-upakovke-yeda-pinteres-30.jpg',
        16);

insert into items (title, description, img_path, price)
values ('Сосиски8',
        'Мясные',
        'https://mig.pics/x/uploads/posts/2022-09/1663817933_23-mykaleidoscope-ru-p-sosiski-v-vakuumnoi-upakovke-yeda-pinteres-30.jpg',
        17);

insert into items (title, description, img_path, price)
values ('Сосиски9',
        'Мясные',
        'https://mig.pics/x/uploads/posts/2022-09/1663817933_23-mykaleidoscope-ru-p-sosiski-v-vakuumnoi-upakovke-yeda-pinteres-30.jpg',
        18);

insert into items (title, description, img_path, price)
values ('Сосиски10',
        'Мясные',
        'https://mig.pics/x/uploads/posts/2022-09/1663817933_23-mykaleidoscope-ru-p-sosiski-v-vakuumnoi-upakovke-yeda-pinteres-30.jpg',
        19);

insert into items (title, description, img_path, price)
values ('Сосиски11',
        'Мясные',
        'https://mig.pics/x/uploads/posts/2022-09/1663817933_23-mykaleidoscope-ru-p-sosiski-v-vakuumnoi-upakovke-yeda-pinteres-30.jpg',
        20);

insert into items (title, description, img_path, price)
values ('Сосиски12',
        'Мясные',
        'https://mig.pics/x/uploads/posts/2022-09/1663817933_23-mykaleidoscope-ru-p-sosiski-v-vakuumnoi-upakovke-yeda-pinteres-30.jpg',
        21);
