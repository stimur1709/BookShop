create function trigger_book2user_up() returns trigger as
'
    begin
        if (old.type_id = 3 and new.type_id in (1, 2, 5)) then
            return old;
        end if;
        return new;
    end;
' LANGUAGE plpgsql;

create trigger trigger_book2user_up_before
    before update
    on book2user
    for each row
execute procedure trigger_book2user_up();