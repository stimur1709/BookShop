create function get_books_viewed(user_id integer)
    returns TABLE
            (
                id            integer,
                discount      double precision,
                image         character varying,
                image_id      integer,
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
                            b.image,
                            b.image_id,
                            b.is_bestseller,
                            b.popularity,
                            b.price,
                            b.slug,
                            b.title,
                            b.pub_date,
                            b.code,
                            b.rate
                     from get_books($1) b
                              join books_viewed bv on b.id = bv.book_id and bv.user_id = $1
                     order by bv.time desc;
    end
';

alter function get_books_viewed(integer) owner to postgres;






