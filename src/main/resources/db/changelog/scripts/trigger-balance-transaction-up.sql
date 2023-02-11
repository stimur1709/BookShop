create function trigger_balance_transaction_up() returns trigger as
'
    begin
        if (new.status_payment_id = 3) then
            update users set balance = balance + new.value where id = new.user_id;
        end if;
        return new;
    end;
'LANGUAGE plpgsql;

create trigger balance_transaction_up_before
    before update
    on balance_transaction
    for each row
execute procedure trigger_balance_transaction_up();