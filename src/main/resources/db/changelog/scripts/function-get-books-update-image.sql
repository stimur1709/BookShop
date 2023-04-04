create or replace function get_books(user_id integer)
    returns TABLE
            (
                id            integer,
                discount      double precision,
                image         character varying,
                is_bestseller smallint,
                popularity    double precision,
                price         integer,
                slug          character varying,
                title         character varying,
                pub_date      date,
                code          character varying,
                rate          numeric
            )
    language plpgsql
as
'
    BEGIN
        return query select b.id,
                            b.discount,
                            i.name,
                            b.is_bestseller,
                            b.popularity,
                            b.price,
                            b.slug,
                            b.title,
                            b.pub_date,
                            coalesce(t.code, '''')       as code,
                            coalesce(avg(br1.rating), 0) as rate
                     from books b
                              left join book2user b2u on b.id = b2u.book_id and b2u.user_id = $1
                              left join book2user_type t on b2u.type_id = t.id
                              left join book_rating br1 on b.id = br1.book_id
                              left join image i on i.id = b.image_id
                     group by b.id, b.discount, i.name, b.is_bestseller, b.popularity,
                              b.price, b.slug, b.title, b.pub_date, t.code;
    end
';







