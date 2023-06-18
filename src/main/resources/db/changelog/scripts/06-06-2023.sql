CREATE EXTENSION "uuid-ossp";

create or replace function trigger_books_b_in() returns trigger
    language plpgsql
as
'
    begin
        if (new.slug is null) then
            new.slug = uuid_generate_v4();
        end if;
        if (new.discount is null) then
            new.discount = 0;
        end if;
        if (new.is_bestseller is null) then
            new.is_bestseller = 0;
        end if;
        if (new.pub_date is null) then
            new.pub_date = now();
        end if;
        return new;
    end;
';

create trigger trigger_books_b_in
    before insert
    on books
    for each row
execute procedure trigger_books_b_in();


create or replace function trigger_books_a_in() returns trigger
    language plpgsql
as
'
    begin

        insert into book_file(hash, path, book_id, type_id)
        VALUES (uuid_generate_v4(), ''/book.pdf'', new.id, 1);
        insert into book_file(hash, path, book_id, type_id)
        VALUES (uuid_generate_v4(), ''/book.epub'', new.id, 2);
        insert into book_file(hash, path, book_id, type_id)
        VALUES (uuid_generate_v4(), ''/book.fb2'', new.id, 3);
        return new;
    end;
';

create trigger trigger_books_a_in
    after insert
    on books
    for each row
execute procedure trigger_books_a_in();