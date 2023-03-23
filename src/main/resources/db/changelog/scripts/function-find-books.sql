create function find_books(user_id integer, search character varying, bestseller boolean, disc boolean, from_date date, to_date date)
    returns TABLE(id integer, discount double precision, image character varying, is_bestseller smallint, popularity double precision, price integer, slug character varying, title character varying, pub_date date, code character varying, rate numeric)
    language plpgsql
as
'
BEGIN
        return query select b.id,
                            b.discount,
                            b.image,
                            b.is_bestseller,
                            b.popularity,
                            b.price,
                            b.slug,
                            b.title,
                            b.pub_date,
                            b.code,
                            b.rate
                     from get_books($1) b
                              left join books b1 on b.id = b1.id
                              left join book2author b2a on b2a.book_id = b.id
                              left join authors a on a.id = b2a.author_id
                              left join book2genre b2g on b2g.book_id = b.id
                              left join genre g on b2g.genre_id = g.id
                              left join book2tag b2t on b2t.book_id = b.id
                              left join tags t on b2t.tag_id = t.id
                     where (case
                                when $2 is not null then upper(b.title) like upper(concat(''%'', $2, ''%''))
                                    or upper(b1.description) like upper(concat(''%'', $2, ''%''))
                                    or upper(a.name) like upper(concat(''%'', $2, ''%''))
                                    or upper(g.name) like upper(concat(''%'', $2, ''%''))
                                    or upper(t.name) like upper(concat(''%'', $2, ''%''))
                                else true end)
                       and (case when $3 = true then b.is_bestseller = 1 else true end)
                       and (case when $4 = true then b.discount > 0 else true end)
                       and (case when $5 is not null and $6 is not null then b.pub_date between $5 and $6 else true end)
                     group by b.id, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title,
                              b.pub_date, b.code, b.rate;
end
';

alter function find_books(integer, varchar, boolean, boolean, date, date) owner to postgres;


