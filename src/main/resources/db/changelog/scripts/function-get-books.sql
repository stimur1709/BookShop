create function get_books(user_id integer)
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
                            i.name,
                            i.id,
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
                     group by b.id, b.discount, i.name, i.id, b.is_bestseller, b.popularity,
                              b.price, b.slug, b.title, b.pub_date, t.code;
    end
';

alter function get_books(integer) owner to postgres;

create function get_books_by_author_slug(user_id integer, a_slug character varying)
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
                              join book2author b2a on b.id = b2a.book_id
                              join authors a on a.id = b2a.author_id
                     where a.slug = $2;
    end
';

alter function get_books_by_author_slug(integer, varchar) owner to postgres;

create function get_books_by_genre_slug(user_id integer, g_slug character varying)
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
                              join book2genre b2g on b.id = b2g.book_id
                              join genre g on g.id = b2g.genre_id
                     where g.slug = $2;
    end
';

alter function get_books_by_genre_slug(integer, varchar) owner to postgres;

create function get_books_by_tag_slug(user_id integer, t_slug character varying)
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
                              join book2tag b2t on b.id = b2t.book_id
                              join tags t on t.id = b2t.tag_id
                     where t.slug = $2;
    end
';

alter function get_books_by_tag_slug(integer, varchar) owner to postgres;

create function get_books_by_slug(user_id integer, book_slug character varying)
    returns TABLE
            (
                id             integer,
                discount       double precision,
                image          character varying,
                image_id       integer,
                is_bestseller  smallint,
                popularity     double precision,
                price          integer,
                slug           character varying,
                title          character varying,
                pub_date       date,
                code           character varying,
                rate           numeric,
                description    text,
                user_rating    integer,
                count1         bigint,
                count2         bigint,
                count3         bigint,
                count4         bigint,
                count5         bigint,
                rate_review    integer,
                download_count integer
            )
    language plpgsql
as
'
    BEGIN
        return query
            select b.id,
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
                   b.rate,
                   b1.description,
                   coalesce(br.rating, 0) as user_rating,
                   count(r1)              as count1,
                   count(r2)              as count2,
                   count(r3)              as count3,
                   count(r4)              as count4,
                   count(r5)              as count5,
                   (case
                        when count(rl.value) = 0 then 0
                        when sum(rl.value) < count(rl.value) * 0.2 then 1
                        when sum(rl.value) >= count(rl.value) * 0.2 and sum(rl.value) < count(rl.value) * 0.4 then 2
                        when sum(rl.value) >= count(rl.value) * 0.4 and sum(rl.value) < count(rl.value) * 0.6 then 3
                        when sum(rl.value) >= count(rl.value) * 0.6 and sum(rl.value) < count(rl.value) * 0.8 then 3
                        else 5 end)       as rate_review,
                   coalesce(fd.count, 0)  as download_count
            from get_books($1) b
                     join books b1 on b.id = b1.id
                     left join book_rating br on b.id = br.book_id and br.user_id = $1
                     left join book2user b2u on b.id = b2u.book_id and b2u.user_id = $1
                     left join book_rating r1 on b.id = r1.book_id and r1.rating = 1
                     left join book_rating r2 on b.id = r2.book_id and r2.rating = 2
                     left join book_rating r3 on b.id = r3.book_id and r3.rating = 3
                     left join book_rating r4 on b.id = r4.book_id and r4.rating = 4
                     left join book_rating r5 on b.id = r5.book_id and r5.rating = 5
                     left join book_review r on b1.id = r.book_id
                     left join book_review_like rl on r.id = rl.review_id
                     left join file_download fd on b1.id = fd.book_id
            where b.slug = $2
            group by b.id, b.discount, b.image, b.image_id, b.is_bestseller, b.popularity,
                     b.price, b.slug, b.title, b.pub_date, b.code, br.rating, b.code, b.rate, b1.description,
                     download_count;
    end
';

alter function get_books_by_slug(integer, varchar) owner to postgres;







