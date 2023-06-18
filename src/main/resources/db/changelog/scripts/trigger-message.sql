create or replace function trigger_message_in() returns trigger
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

create trigger trigger_message_in
    before insert
    on message
    for each row
execute procedure trigger_message_in();
