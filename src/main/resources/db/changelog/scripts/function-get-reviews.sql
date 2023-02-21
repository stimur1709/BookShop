create function get_reviews(book_id character varying, user_id integer)
    returns TABLE
            (
                id       integer,
                text     text,
                date     timestamp without time zone,
                name     text,
                value    smallint,
                likes    bigint,
                dislikes bigint
            )
    language plpgsql
as
'
BEGIN
    return query select br.id,
                        br.text,
                        br.time,
                        u.lastname || '' '' || u.firstname                 as name, brl1.value,
                        count(brl2.value) filter ( where brl2.value = 1 )  as likes,
                        count(brl2.value) filter ( where brl2.value = -1 ) as dislikes
                 from book_review br
                          left join book_review_like brl1 on br.id = brl1.review_id and brl1.user_id = $2
                          left join book_review_like brl2 on br.id = brl2.review_id
                          join users u on u.id = br.user_id
                          join books b on b.id = br.book_id
                 where b.slug = $1
                 group by br.id, br.text, br.time, name, brl1.value;
end
';

alter function get_reviews(varchar, integer) owner to postgres;

