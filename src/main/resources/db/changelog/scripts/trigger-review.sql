create or replace function trigger_book_review_in() returns trigger
    language plpgsql
as
'
    begin
        if (new.time is null) then
            new.time = now();
        end if;
        return new;
    end;
';

create trigger trigger_book_review_in
    before insert
    on book_review
    for each row
execute procedure trigger_book_review_in();
