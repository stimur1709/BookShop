create function trigger_balance_transaction_in() returns trigger
    language plpgsql
as
'
begin
    if (new.status_payment_id = 3) then
        insert into book2user(book_id, user_id, type_id)
        VALUES (new.book_id, new.user_id, 3)
        on conflict(book_id, user_id) do update set type_id = 3;
        update users set balance = balance - new.value where id = new.user_id;
    end if;
    return new;
end;
';

create trigger balance_transaction_in_before
    before insert
    on balance_transaction
    for each row
execute procedure trigger_balance_transaction_in();

