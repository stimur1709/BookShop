create function trigger_balance_transaction_up() returns trigger as
'
    begin
        if (new.status_payment_id = 3) then
            update book2user
            set type_id = 3
            where book_id = old.book_id
              and user_id = old.user_id;
        end if;
        return new;
    end;
'LANGUAGE plpgsql;

create trigger balance_transaction_up_before
    before update
    on balance_transaction
    for each row
execute procedure trigger_balance_transaction_up();