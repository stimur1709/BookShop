create function get_recommended_books(user_id integer)
    returns TABLE(id integer, discount double precision, image character varying, is_bestseller smallint, popularity double precision, price integer, slug character varying, title character varying, pub_date date, code character varying, rate numeric, sort_index integer)
    language plpgsql
as
'
declare
    count_books      integer;
    count_books_user integer;
BEGIN
    select count(*)
    from books
    into count_books;

    select count(*) from book2user b where b.user_id = $1 into count_books_user;

    if (count_books_user > 0) then
        return query select distinct on (b.title) b.id,
                                                  b.discount,
                                                  b.image,
                                                  b.is_bestseller,
                                                  b.popularity,
                                                  b.price,
                                                  b.slug,
                                                  b.title,
                                                  b.pub_date,
                                                  b.code,
                                                  b.rate,
                                                  3 as sort_index
                     from get_books($1) b
                              join book2author ba on b.id = ba.book_id
                              join authors a on ba.author_id = a.id
                              join book2genre b2g on ba.book_id = b2g.book_id
                              join genre g on b2g.genre_id = g.id
                              join book2tag b2t on b2g.book_id = b2t.book_id
                              join tags t on t.id = b2t.tag_id
                     where b.code not in (''KEPT'', ''PAID'', ''CART'', ''ARCHIVED'')
                       and (a.name in (select distinct a.name
                                       from authors a
                                                join book2author b2a on a.id = b2a.author_id
                                                join books b2 on b2.id = b2a.book_id
                                                join book2user b2u
                                                     on b2.id = b2u.book_id and b2u.user_id = $1
                                       limit count_books * 0.3 offset 0)
                         or g.name in (select distinct g.name
                                       from genre g
                                                join book2genre b2g on a.id = b2g.genre_id
                                                join books b2 on b2.id = b2g.book_id
                                                join book2user b2u
                                                     on b2.id = b2u.book_id and b2u.user_id = $1
                                       limit count_books * 0.3 offset 0)
                         or t.name in (select distinct t.name
                                       from tags t
                                                join book2tag b2t on a.id = b2t.tag_id
                                                join books b2 on b2.id = b2g.book_id
                                                join book2user b2u
                                                     on b2.id = b2u.book_id and b2u.user_id = $1
                                       limit count_books * 0.3 offset 0))
                     union all
                     select distinct
                         on (b.title) b.id,
                                      b.discount,
                                      b.image,
                                      b.is_bestseller,
                                      b.popularity,
                                      b.price,
                                      b.slug,
                                      b.title,
                                      b.pub_date,
                                      b.code,
                                      b.rate,
                                      2 as sort_index
                     from get_books($1) b
                              join book2author ba
                                   on b.id = ba.book_id
                              join authors a on ba.author_id = a.id
                              join book2genre b2g on ba.book_id = b2g.book_id
                              join genre g on b2g.genre_id = g.id
                              join book2tag b2t on b2g.book_id = b2t.book_id
                              join tags t on t.id = b2t.tag_id
                     where b.code not in (''KEPT'', ''PAID'', ''CART'', ''ARCHIVED'')
                       and (a.name in (select distinct a.name
                                       from authors a
                                                join book2author b2a on a.id = b2a.author_id
                                                join books b2 on b2.id = b2a.book_id
                                                join book2user b2u
                                                     on b2.id = b2u.book_id
                                                         and b2u.user_id = $1
                                       limit count_books * 0.6 offset count_books * 0.3)
                         or g.name in (select distinct g.name
                                       from genre g
                                                join book2genre b2g on a.id = b2g.genre_id
                                                join books b2 on b2.id = b2g.book_id
                                                join book2user b2u
                                                     on b2.id = b2u.book_id
                                                         and b2u.user_id = $1
                                       limit count_books * 0.6 offset count_books * 0.3)
                         or t.name in (select distinct t.name
                                       from tags t
                                                join book2tag b2t on a.id = b2t.tag_id
                                                join books b2 on b2.id = b2g.book_id
                                                join book2user b2u
                                                     on b2.id = b2u.book_id
                                                         and b2u.user_id = $1
                                       limit count_books * 0.6 offset count_books * 0.3))
                     union all
                     select b.id,
                            b.discount,
                            b.image,
                            b.is_bestseller,
                            b.popularity,
                            b.price,
                            b.slug,
                            b.title,
                            b.pub_date,
                            b.code,
                            b.rate,
                            1 as sort_index
                     from get_books($1) b
                     order by sort_index desc;
    else
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
                            b.rate,
                            0
                     from get_books($1) b
                     order by rate, pub_date desc;
    end if;
end
';

alter function get_recommended_books(integer) owner to postgres;

