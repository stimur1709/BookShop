create function trigger_book2user_up_b() returns trigger as
'
    begin
        if (old.type_id in (3, 4) and new.type_id in (1, 2, 5)) then
            return old;
        end if;
        return new;
    end;
' LANGUAGE plpgsql;

create trigger trigger_book2user_up_before
    before update
    on book2user
    for each row
execute procedure trigger_book2user_up_b();

create function trigger_book2user_in() returns trigger as
'
    begin
        update books
        set popularity = popularity + case
                                          when new.type_id = 1 then 0.4
                                          when new.type_id = 2 then 0.7
                                          when new.type_id in (3, 4) then 1
                                          else 0 end
        where id = new.book_id;
        return new;
    end;
' LANGUAGE plpgsql;

create trigger trigger_book2user_in_after
    after insert
    on book2user
    for each row
execute procedure trigger_book2user_in();

create function trigger_book2user_up_a() returns trigger as
'
    begin
        update books
        set popularity = popularity + case
                                          when old.type_id = 2 and new.type_id = 1 then -0.3
                                          when old.type_id = 1 and new.type_id = 5 then -0.4
                                          when old.type_id = 2 and new.type_id = 3 then 0.3
                                          when old.type_id = 2 and new.type_id = 5 then -0.7
                                          when old.type_id = 1 and new.type_id = 2 then 0.3
                                          when old.type_id = 1 and new.type_id = 3 then 0.6
                                          when old.type_id = 5 and new.type_id = 1 then 0.4
                                          when old.type_id = 5 and new.type_id = 2 then 0.7
                                          when old.type_id = 5 and new.type_id in (3, 4) then 1
                                          when old.type_id in (3, 4) and new.type_id = 5 then -0.7
                                          else 0 end
        where id = new.book_id;
        return new;
    end;
' LANGUAGE plpgsql;

create trigger trigger_book2user_up_after
    after update
    on book2user
    for each row
execute procedure trigger_book2user_up_a();
