create function find_books(user_id integer, search varchar)
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
                     where upper(b.title) like upper(concat(''%'', $2, ''%''))
                        or upper(b1.description) like upper(concat(''%'', $2, ''%''))
                        or upper(a.name) like upper(concat(''%'', $2, ''%''))
                        or upper(g.name) like upper(concat(''%'', $2, ''%''))
                        or upper(t.name) like upper(concat(''%'', $2, ''%''))
                     group by b.id, b.discount, b.image, b.is_bestseller, b.popularity, b.price, b.slug, b.title,
                              b.pub_date, b.code, b.rate;
end
';

alter function find_books(integer, varchar) owner to postgres;

