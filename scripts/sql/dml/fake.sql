insert into "ORG"(is_school, email, hashed_password, events)
values  (false , 'huodon@gmail.com', '0', '{1,2,3,4,5}');

select array_append(ARRAY[1,2], 3);