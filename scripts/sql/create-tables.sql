-- -----------------------------
create type "SEX" as enum ('F', 'M');

create table "USER" (
  ID              serial,
  SEX             "SEX", -- F/M
  CERTIFIED       bool, -- 已认证?
  LOGIN_NAME      varchar(16) unique      not null, -- (TODO email/mobile login)
  REAL_NAME       varchar(32),
  NICH_NAME       varchar(32),
  AVATAR          varchar(255),
  MOBILE          varchar(11) unique      not null,
  EMAIL           varchar(128),
  BRITH           date,
  HASHED_PASSWORD varchar(128)            not null,
  CITY            int,
  USER_SOCIAL     int,
  primary key (ID)
);

-- TODO implement cond insert
insert into "USER" (LOGIN_NAME, MOBILE, HASHED_PASSWORD) values
  ('admin', '11111111111', '11111111');

create table "ADDRESS" (
  ID     serial,
  CITY   varchar(255),
  AREA   smallint,
  STREET varchar(255)
);

create table "USER_SOCIA___" (
  id                   serial,
  user_id              int       not null,
  rel_org_ids          int array not null default '{}' :: int [], --
  --TODO role                 varchar(5), -- 用户角色(或角色组合)
  watched_event_ids    int array not null default '{}' :: int [], --
  signed_event_ids     int array not null default '{}' :: int [], --
  commented_event_ids  int array not null default '{}' :: int [], --
  watched_venue_ids    int array not null default '{}' :: int [], --
  joined_topic_ids     int array not null default '{}' :: int [], --
  posted_threads_ids   int array not null default '{}' :: int [], --
  joined_events        int array not null default '{}' :: int [], --

  commented_thread_ids int array not null default '{}' :: int [], --
  watched_user_ids     int array not null default '{}' :: int [], --
  fans_user_ids        int array not null default '{}' :: int [], --
  blocked_user_ids     int array not null default '{}' :: int [], --
  parents_ids          int array not null default '{}' :: int [], --
  primary key (user_id)
);


drop table "USER_SOCIAL";
create table "USER_SOCIAL" (
  uid           int    not null,
  joined_events int [] not null default '{}' :: int []

);

create or replace function user_join_event(user_id int, event_id int)
  returns int as $$
begin

  update "USER_SOCIAL"
  set joined_events = array_append(joined_events, event_id)
  where uid = user_id;

end;
$$ language plpgsql;
select user_join_event(0, 1);

drop table "USER_EVENT";
create table "USER_EVENT" (
  id          serial,
  username    varchar(128) not null,
  eventid     int          not null,
  eventname   varchar(255) not null,
  joined_time date         not null
);


drop table "USER_EVENT_LITTLE";
create table "USER_EVENT_LITTLE" (
  uid   int not null,
  event int not null
);

-- select '2015-08-10T00:01:08.883' :: timestamp;

-- 用户详细信息, 实名认证
create table "USER_IDENTITY_INFO" (
  user_id  int          not null,
  photo    varchar(255),
  id_type  varchar(16)  not null,
  id_value varchar(128) not null,
  nation   int,
  primary key (user_id)
);

create table "USER_TRACK_INFO" (
  user_id         int  not null,
  register_time   date not null,
  last_login_time timestamp,
  last_login_ip   inet, -- 最后登陆 IP
  primary key (USER_ID)
);

create table "USER_EVOLUTION" (
  id serial,
  primary key (id)
);

drop table "ORG" cascade;
create table "ORG" (
  id              serial,
  is_school       boolean      not null,
  email           varchar(128) not null,
  hashed_password varchar(255) not null,
  profiles        int,
  EVENTS          int [],
  primary key (email)
);
insert into "ORG" (is_school, email, hashed_password) values
  (false, 'admin@gmail.com', '11111111');

drop table "ORG_PROFILES" cascade;
create table "ORG_PROFILES" (
  id             serial,
  org            varchar(128) not null,
  name           varchar(64)  not null,
  address        varchar(255),
  tel            varchar(64)  not null,
  province       varchar(64)  not null,
  city           varchar(64)  not null,
  nature         varchar(64),
  code           varchar(64)  not null,
  leaderName     varchar(64)  not null,
  leaderId       varchar(64)  not null,
  personnelCount int,
  tags           varchar(255),
  parent         varchar(256),
  image          varchar(128),
  safe           varchar(64),
  org_type       varchar(128) not null default 'None',
  classify       varchar(128) not null default 'None',
  summary        varchar(64)
);
alter table "ORG_PROFILES" add column org_type varchar(128) not null default 'None';

alter table "ORG_PROFILES" add column classify varchar(128) not null default 'None';
alter table "ORG_PROFILES" alter column tags type varchar(1024);
alter table "ORG_PROFILES" alter column summary type text;

drop table "ORG_NOTICE";
create table "ORG_NOTICE" (
  id      serial,
  org     varchar(64) not null,
  content text        not null,
  created date        not null
);


drop table "EVENT" cascade;
alter table "EVENT" add column enabled boolean;
create table "EVENT" (
  id                 serial,
  org                varchar(128) not null,
  name               varchar(255) not null,
  enabled            boolean      not null default true,
  classify           varchar(64)  not null,
  form               varchar(64)  not null,
  tag                varchar(64),
  image              varchar(64)  not null,
  summary            text,
  age_limit_min      int,
  age_limit_max      int,
  time_span          varchar(64),
  allow_parents_join boolean,
  need_manager       boolean,
  notice             text,
  safeinfo           text,
  publish_count      int                   default 0,
  primary key (name)
);

drop table "EVENT_REVIEW";
create table "EVENT_REVIEW" (
  id             serial,
  name           varchar(255) primary key,
  relation_event varchar(255) not null,
  relation_org   varchar(128) not null,
  stay_top       bool         not null default false,
  content        text         not null,
  image          varchar(255)
);
insert into "EVENT_REVIEW" (name, relation_event, relation_org, stay_top, content, image)
values ('test', 'publish event', 'admin@gmail.com', false, '', 'test');


drop table "ORG_LETTER";
create table "ORG_LETTER" (
  id      serial,
  is_read boolean      not null default false,
  created varchar(64),
  title   varchar(128) not null,
  content text         not null,
  org     varchar(128) not null
);


drop table "IMAGES_STORE";
create table "IMAGES_STORE" (
  id   serial,
  name varchar(64) not null primary key,
  meta varchar(64)
);

drop table "ORG_COUNSELLOR" cascade;
alter table "ORG_COUNSELLOR" add column enabled boolean;
create table "ORG_COUNSELLOR" (
  id      serial,
  org     varchar(128) not null,
  enabled boolean      not null default false,
  index   int                   default 10,
  show    boolean      not null default false,
  name    varchar(128) not null
);

drop table "PUBLISHED_EVENT" cascade;
alter table "PUBLISHED_EVENT" add column change_cause varchar(512);
alter table "PUBLISHED_EVENT" add column name varchar(255);
create table "PUBLISHED_EVENT" (
  id              serial,
  org             varchar(128) not null,
  status          varchar(32),
  change_cause    varchar(512),
  template        varchar(128) not null,
  name            varchar(255) not null,
  venues          varchar(128) not null,
  start_time      varchar(64)  not null,
  end_time        varchar(64)  not null,
  people_limit    int          not null,
  counsellor      varchar(64),
  image           varchar(255),
  origin_price    int          not null,
  current_price   int          not null,
  insurance_price int          not null,
  price_explain   text         not null
);

drop table "VENUES";
--- alter table "VENUES" add location point;--- 
create table "VENUES" (
  id         serial,
  org        varchar(128) not null,
  name       varchar(128) not null,
  area       int,
  cap        int,
  by         varchar(32),
  location   varchar(256),
  image      varchar(256),
  image_type varchar(16)
);
--- DICT ---
drop table "EVENT_CLASSIFY" cascade;
create table "EVENT_CLASSIFY" (
  id    serial,
  value varchar(64) not null,
  primary key (value)
);


create table "ORG_CERT" (
  org   varchar(128),
  image varchar(256),
  primary key (image)
);

drop table "INFO_META" cascade;
create table "INFO_META" (
  name varchar(256),
  obj  varchar(128),
  ext  varchar(256),
  ext2 varchar(256)
    primary key (image)
);

insert into "EVENT_CLASSIFY" (value) values
  ('?义'),
  ('?养'),
  ('?化'),
  ('?存'),
  ('?务'),
  ('?养'),
  ('?养'),
  ('?动'),
  ('?境'),
  ('?验');

------------------------------------------------------------
drop table "DICT_EVENT_TYPE" cascade;
create table "DICT_EVENT_TYPE" (
  id    serial,
  value varchar(64) not null,
  primary key (value)
);

drop table "ORG_FUCTION";
create table "ORG_FUCTION" (
  counsellor jsonb
);

create table "ORG_MANAGER" (
  org     varchar(128),
  enabled boolean,
  "user"  varchar(128)
);

alter table "ORG_MANAGER" add column enabled boolean;

insert into "DICT_EVENT_TYPE" (value) values
  ('?观'),
  ('?习'),
  ('?践'),
  ('?究');
------------------------------------------------------------
drop table "DICT_EVENT_VERIFY_STATUS" cascade;
create table "DICT_EVENT_VERIFY_STATUS" (
  id    serial,
  value varchar(64) not null,
  primary key (value)
);

insert into "DICT_EVENT_VERIFY_STATUS" (value) values
  ('待审核'),
  ('已审核'),
  ('审核失败');
------------------------------------------------------------
drop table "DICT_EVENT_PROCESS" cascade;
create table "DICT_EVENT_PROCESS" (
  id    serial,
  value varchar(32) not null,
  primary key (value)
);
insert into "DICT_EVENT_PROCESS" (value) values
  ('?中'),
  ('?中'),
  ('?束'),
  ('?消');

------------------------------------------------------------
drop table "DICT_EVENT_STRATEGY" cascade;
create table "DICT_EVENT_STRATEGY" (
  id    serial,
  value varchar(32) not null,
  primary key (value)
);

insert into "DICT_EVENT_STRATEGY" (value) values
  ('?亲'),
  ('?快'),
  ('?本'),
  ('?达');
------------------------------------------------------------
drop table "DICT_EVENT_STRATEGY_STATUS" cascade;
create table "DICT_EVENT_STRATEGY_STATUS" (
  id    serial,
  value varchar(32),
  primary key (value)
);

insert into "DICT_EVENT_STRATEGY_STATUS" (value) values
  ('已发布'),
  ('已过期'),
  ('已取消');

------------------------------------------------------------
drop table "DICT_EVENT_COST" cascade;
create table "DICT_EVENT_COST" (
  id    serial,
  value varchar(32),
  primary key (value)
);
insert into "DICT_EVENT_COST" (value) values
  ('免费'),
  ('收费');
------------------------------------------------------------
drop table "DICT_ORG_TYPE" cascade;
create table "DICT_ORG_TYPE" (
  id    serial,
  value varchar(32),
  primary key (value)
);
insert into "DICT_ORG_TYPE" (value) values
  ('其?'),
  ('企?'),
  ('政?');
------------------------------------------------------------
drop table "DICT_ORG_CLASSIFY" cascade;
create table "DICT_ORG_CLASSIFY" (
  id    serial,
  value varchar(32),
  primary key (value)
);
insert into "DICT_ORG_CLASSIFY" (value) values
  ('博?'),
  ('纪?'),
  ('展?'),
  ('史?'),
  ('遗?'),
  ('烈?'),
  ('文?'),
  ('美?'),
  ('图?'),
  ('科?'),
  ('少?'),
  ('科?'),
  ('生?'),
  ('社?'),
  ('体?'),
  ('企?'),
  ('综?'),
  ('培?');
------------------------------------------------------------
-- 办学性质
drop table "DICT_SCHOOL_NATURE" cascade;
create table "DICT_SCHOOL_NATURE" (
  id    serial,
  value varchar(32),
  primary key (value)
);
insert into "DICT_SCHOOL_NATURE" (value) values
  ('?公'),
  ('?民');

------------------------------------------------------------
-- 学校等级
drop table "DICT_SCHOOL_LEVEL" cascade;
create table "DICT_SCHOOL_LEVEL" (
  id    serial,
  value varchar(32),
  primary key (value)
);

insert into "DICT_SCHOOL_LEVEL" (value) values
  ('省?'),
  ('市?');


create table "PLAYGROUND" (
  js jsonb

);

create or replace function json_update(data json, key text, value json)
  returns json
as $$
    from json import loads, dumps
    if key is None: return data
    js = load(data)
    js[key] = value
    return dump(js)
    $$ language plpython3u;


drop table "STRATEGY" cascade;
create table "STRATEGY" (
  id       serial,
  evevt    int          not null,
  title    varchar(255) not null,
  content  text,
  img      varchar(255),
  pub_time date         not null,
  click    int default 0,
  likes    int default 0,
  org      varchar(255) not null,
  primary key (id)
);
 
