sc-- -----------------------------

-- FIXME
-- 跨 scehma 同名的表对 DAO 会造成困扰
-- create schema if not exists dict;
-- create schema if not exists org;
-- create schema if not exists admin;
-- create schema if not exists system;
-- create schema if not exists event;
-- create schema if not exists person;
-- create schema if not exists topic;
-- create schema if not exists site;
-- -----------------------------
create type enum_org_type as enum (
  'School', 'Enterprise'
);

-- TODO level list?
create type enum_schools_level as enum (
  'N', 'A', 'B', 'C'
);

--
-- [S]?, [P]?, [C]?, [SC]?, [PC]?
create type enum_person_role as enum (
  'S', 'P', 'C', 'PC', 'SC'
);

create type enum_event_publisher_type as enum (
  'School', 'Enterprise', 'Person'
);

create type enum_role as enum (
  'None', 'Student', 'Parents', 'Counsellors', 'School', 'Enterprise'
);

create type enum_school_phase as enum ( 'Nursery', 'Elementary', 'Secondary' );

-- 性别 [M]男 | [F]女
create type enum_sex as enum ( 'M', 'F' );

-- 证件类型: 身份证 | 护照
create type enum_certificate_type as enum (
  'Id', -- 身份证
  'Passport' -- 护照
);
-- -----------------------------

create table users (
  id              serial,
  disabled        bool default false, -- 账号禁用
  activated       bool default false, -- 账吨未激活(email)
  sex             enum_sex, -- 性别
  certified       bool, -- 已认证?
  login_name      varchar(16)         not null, -- 登陆名(TODO 使用邮箱/电话登陆)
  real_name       varchar(32), -- 真实姓名
  nich_name       varchar(32), -- 昵称
  avatar          varchar(255), -- 头像(base16? url?)
  mobile          varchar(11) unique  not null, -- 手机
  email           varchar(128) unique not null,
  brith           date, -- 生日
  provide_id      varchar(32),
  provide_key     varchar(32),
  hashed_password varchar(128)        not null, -- 密码
  city            int, -- 所在城市
  area            smallint, -- 所在区
  street          varchar(255), -- 街道
  user_social     int, -- 社交信息
  primary key (id)
);

drop table login_info;
create table login_info(
  id int,
  provide_id      varchar(32),
  provide_key     varchar(32),
  primary key (id)
)
-- 使用社交关系
create table user_social (
  id                   serial,
  user_id              int not null,
  org_ids              int [], --
  role                 enum_person_role, --
  watched_event_ids    int [], --
  signed_event_ids     int [], --
  commented_event_ids  int [], --
  watched_venue_ids    int [], --
  joined_topic_ids     int [], --
  posted_threads_ids   int [], --
  commented_thread_ids int [], --
  watched_user_ids     int [], --
  fans_user_ids        int [], --
  blocked_user_ids     int [], --
  parents_ids          int [], --
  primary key (user_id)
);

create table user_consume (
  user_id int not null,
  bills   int [],
  primary key (user_id)
);

create table user_bill (
  id      serial,
  produce int       not null,
  cost    float     not null,
  time    timestamp not null
);

create table user_detail_info (
  user_id  int                   not null,
  photo    varchar(255),
  id_type  enum_certificate_type not null, --身份证号/护照号
  id_value varchar(128)          not null,
  nation   int, -- 民族
  primary key (user_id)
);

create table user_track_info (
  user_id         int  not null,
  register_time   date not null,
  last_login_time timestamp,
  last_login_ip   inet, -- 最后登陆 IP
  primary key (user_id)
);


-- (TODO)
create table user_evolution (
  id serial,
  primary key (id)
);


create table user_activity (
  id    serial,
  event varchar(255), -- 事件
  link  varchar(255), -- 链接
  time  timestamp,
  primary key (id)
);

create table counsellors (
  user_id     int not null,
  star_rate   smallint default 100,
  serve_event int [], -- 服务的活动
  speciality  varchar(32), -- 特长
  work_age    int, -- 工作年龄
  summary     text, -- 简介
  primary key (user_id)
);

create table student (
  id         serial,
  user_id    int not null, -- fk by person.user(id)
  student_id int not null, --
  school_id  int not null, -- fk by org.schools(id)
  class      int, --
  grade      int, --
  primary key (id)
);
-- -------------------------------
create table org (
  id              serial,
  code            int          not null, --
  name            varchar(255) not null,
  logo            varchar(255) not null,
  certified       bool         not null default false, -- 已认证?
  type            enum_org_type, --
  empolyee_count  int                   default 10,
  safe_lavel      int                   default 0,
  city            int          not null, -- 市
  area            int          not null, -- 区
  street          varchar(128) not null, -- 街道地址
  street_code     int          not null, -- 街道号
  geo_position    point,
  contact_name    varchar(6)   not null, -- 联系人
  contact_tel     varchar(11)  not null,
  principal_name  varchar(32)  not null, -- 负责人姓名
  principal_id_no varchar(18)  not null default '', -- 负责人身份证号
  description     varchar(128), -- 描述
  counsellors     int [], -- 辅导员
  cert            int [], -- 证书
  star_rate       int                   default 100, -- 缩合评分
  primary key (id)
);

create table enterprise (
  id         serial,
  org_id     int          not null,
  superior   varchar(255) not null, -- 上级机构
  categories int [],
  primary key (id)
);

create table enterprise_categories (
  id       serial,
  category varchar(32) not null,
  primary key (id)
);

create table org_certificate (
  id           serial,
  org_id       int          not null,
  cert_name    varchar(255) not null,
  cert_summary text,
  primary key (id)
);

create table org_certifying (
  id              serial,
  org_id          int not null,
  phase           int not null default 0,
  failure_code    int, -- fk by dict.error(id)
  certifying_time timestamp,
  executor        int not null,
  primary key (id)
);
create table school (
  org_id int not null,
  phase  int not null, -- enum_school_phase
  level  enum_schools_level,
  nature varchar(8),
  admins int [],
  primary key (org_id)
);

create table school_notice (
  id        serial,
  school_id int,
  title     varchar(255),
  content   text,
  time      timestamp,
  primary key (id)
);

create table schools_activity (
  id    serial,
  time  timestamp,
  event varchar(255),
  primary key (id)
);

create table org_admin (
  id              serial,
  org_id          int          not null,
  user_id         int          not null,
  hashed_password varchar(255),
  salt            varchar(233) not null,
  join_time       timestamp,
  last_login_time timestamp,
  primary key (id)
);

create table venues (
  id              int          not null,
  org_id          int          not null,
  name            varchar(255) not null,
  capability      int          not null, -- 容量
  area            int          not null, -- 面积
  tag_ids         int [],
  geo_position    point, -- 地理坐标
  watched_counter int, -- 当用户观注时, 计数用于统计, 热闹度由活动引用量和观注度计算
  address         varchar(255), -- 地址
  primary key (id)
);

create table venues_tag (
  id  serial,
  tag varchar(32),
  primary key (id)
);
-- --------------------------------
-- events

create table event (
  id            int          not null,
  image_url     varchar(255) not null, --
  tag_ids       int []       not null, --
  themes        int [],
  categories    int [],
  summary       text,
  create_time   time         not null,
  modify_time   time,
  classify      int, -- 分类
  age_uplimit   int,
  age_downlimit int,
  primary key (id)
);

create table event_subject (
  id        serial,
  subject   varchar(32),
  event_ids int [15],
  primary key (id)
);

create table event_tags (
  id  serial,
  tag varchar(32),
  primary key (id)
);

create table event_classify (
  id    serial,
  class varchar(32),
  primary key (id)
);

create table event_published (
  id                      int                       not null,
  event_id                int                       not null,
  venue_id                int                       not null,
  collection_time         timestamp                 not null, --
  collection_address      varchar(255)              not null, --
  collection_geo_position point, --
  sign_end_time           timestamp                 not null, --
  sign_summary            varchar(255),
  cost_origin             float, --
  cost_now                float, --
  premium                 float, --
  safe_summary            varchar(255),
  publisher_type          enum_event_publisher_type not null, -- '0: school, 1: businesses, 2: person',
  publisher_id            int                       not null,
  publisher_name          varchar(8)                not null,
  publisher_tel           varchar(11),
  required_parents        bool default false,
  required_counsellors    bool default false,
  joined_user             int []                    not null, -- 预订数
  counsellors             int [],
  status                  int, --
  canceled                bool, -- '已取消?',
  canceled_cause          varchar(255), -- 取消理由
  canceled_time           time, -- 取消时间
  ended                   bool, -- 是否已结束
  ended_time              timestamp, -- 结束时间
  primary key (id)
);

create view event_hots (
    rank,
    event_id
) as
  select
    count(1) as frequency,
    event_id
  from event_published
  group by event_id
  order by frequency desc
  limit 50;

-- 活动评论(可能会使用 MongoDB)
create table event_comment (
  id          serial,
  user_id     int not null,
  event_id    int not null,
  star_rate   int default 100,
  comtent     jsonb,
  modify_time timestamp,
  primary key (id)
);

-- --------------------------------
create table system_admin (
  id              serial,
  user_id         int          not null,
  disabled        bool         not null,
  hashed_password varchar(255) not null, -- 哈希密码
  salt            varchar(128) not null, -- 盐
  join_time       timestamp, -- 加入时间
  last_login_time timestamp, -- 最后登陆时间
  last_login_ip   inet,
  primary key (id)
);


-- --------------------------------

create table topic_topics (
  id     serial,
  name   varchar(32) not null,
  image  varchar(255),
  sumary text,
  primary key (id)
);

create table topic_threads (
  id          int          not null,
  user_id     int          not null,
  topic_id    int          not null,
  title       varchar(235) not null,
  content     text         not null,
  post_time   date         not null,
  modify_time date         not null,
  primary key (id)
);

-- --------------------------------

create table site_news (
  id        serial,
  summary   varchar(512),
  image     varchar(128),
  title     varchar(128),
  --   format    int not null, -- '0: markdown, 1: html',
  content   text, -- 'markdown? html?',
  post_time date,
  primary key (id)
);

create table site_notify (
  id     serial,
  "from" varchar(128) not null default '-', -- linked text
  title  varchar(128) not null default '-',
  body   varchar(255)          default '', -- rich text
  time   date,
  link   varchar(255),
  primary key (id)
);

-- --------------------------------
create table dict_province (
  id         serial,
  name       varchar(8) not null,
  pinyin     varchar(16),
  code_start int        not null,
  code_end   int        not null,
  primary key (id)
);

create table dict_city (
  code    int        not null,
  name    varchar(8) not null,
  pingyin varchar(16),
  primary key (code)
);

create table dict_country (
  id   serial,
  name varchar(32) not null,
  primary key (id)
);

create table dict_nation (
  id      serial,
  code    varchar(32)  not null,
  name    varchar(32)  not null,
  pingyin varchar(128) not null,
  primary key (id)
);

create table dict_event_tags (
  id  serial,
  tag varchar(32),
  primary key (id)
);

create table dict_event_themes (
  id  serial,
  tag varchar(32),
  primary key (id)
);

create table dict_event_categories (
  id        serial,
  categorie varchar(32),
  primary key (id)
);

create table dict_error (
  code     int not null,
  descript varchar(255),
  primary key (code)
);
-- --------------------------------
create table system_application_settings (
  id serial,
  primary key (id)
);


