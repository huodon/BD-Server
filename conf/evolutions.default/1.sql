# --- !Ups

create Test (name varchar  not null primary key)

# --- !Downs

drop table Test;
