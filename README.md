# BD Server (legacy code backup)

## Prerequire

- JDK8
- SBT
- Postgresql
- Git


## Get

### Clone from git

```bash
$ git clone https://git.domain.com/BD-Server
```

### Init database

1. Connect to db

```bash
$ psql -h <host> -U <username> --password
```

2. Execute DDL

```bash
$ cd ./BD-Server/scripts/ddl
$ psql -h <host> -U username --password -f bd-pg.sql
```

### Change server conf

```bash
$ cd ./bandou-server/conf
$ vim application.conf
```

### `application.conf`

| key                           | description          |
|-------------------------------|----------------------|
| slick.dbs.default.db.driver   | JDBC driver |
| slick.dbs.default.db.url      | db URL       |
| slick.dbs.default.db.user     | db username     |
| slick.dbs.default.db.password | db password       |



### Deploy

```
$ cd BD-Server

## compile and package
activator clean stage

## compress as archive
tar -cvzf stage.tar.gz ./stage
```

### Startup

```

$ cd BD-Server/stage
$ ./bin/run
```

---- 

## Nginx configure

### Create

```
vim /etc/nginx/site-enable/bd-server

```

### conf

```

http {
  server {
    localtion / {

  }
  }
}
```

###  Reload nginx conf

```bash
nginx -t # test syntax error
nginx -s reload # reload changed conf
```

## TODO

- Email config
- Split ddl script
- Auth API
