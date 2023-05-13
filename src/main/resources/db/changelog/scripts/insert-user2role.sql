insert into user2role(user_id, role_id)
select u.id, r.id from users u
                           cross join user_role r
where r.role = 'ROLE_USER';