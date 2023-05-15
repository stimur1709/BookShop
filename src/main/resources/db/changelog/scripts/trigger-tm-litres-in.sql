create or replace function trigger_tm_litres_in() returns trigger
    language plpgsql
as
'
    begin
        new.last_time = now();
        return new;
    end;
';

create trigger trigger_tm_litres_in_before
    before insert
    on tm_litres
    for each row
execute procedure trigger_tm_litres_in();

create or replace function trigger_tm_litres_up() returns trigger
    language plpgsql
as
'
    begin
        new.last_time = now();
        return new;
    end;
';

create trigger trigger_tm_litres_up_before
    before update
    on tm_litres
    for each row
execute procedure trigger_tm_litres_up();

