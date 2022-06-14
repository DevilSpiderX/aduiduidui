## API 标准

所有 API 均返回 `msg`, `code`, `data` 三个属性.

| code | 描述   |
|:----:|------|
|  0   | 请求成功 |
|  !0  | 请求失败 |

当 `code == 0` 时, `data` 中为请求所需数据。

当 `code != 0` 时, 应当将 `msg` 中的内容作为参考值。

## 用户登录

### 请求URL

`/api/user/login` `POST`

### 请求参数

| 参数名      | 描述   | 是否必须 | 参考值 |
|----------|------|------|-----|
| uid      | 用户id | 是    ||
| password | 密码   | 是    ||

### 请求

```json
{
  "uid": "uid",
  "password": "pwd"
}
```

### 响应

```json
{
  "code": 0,
  "msg": "登录成功",
  "data": {}
}
```

## 用户退出

### 请求URL

`/api/user/logout` `POST`

### 请求参数

| 参数名      | 描述   | 是否必须 | 参考值 |
|----------|------|------|-----|

### 请求

```json
{}
```

### 响应

```json
{
  "code": 0,
  "msg": "退出成功",
  "data": {}
}
```

## 用户注册

### 请求URL

`/api/user/register` `POST`

### 请求参数

| 参数名         | 描述   | 是否必须 | 参考值          |
|-------------|------|------|--------------|
| uid         | 用户id | 是    ||
| password    | 密码   | 是    ||
| username    | 用户名  | 否    ||
| sex         | 性别   | 否    | M - 男, F - 女 |
| information | 简介信息 | 否    ||

### 请求

```json
{
  "uid": "uid",
  "password": "pwd",
  "username": "username",
  "sex": "M",
  "information": "information"
}
```

### 响应

```json
{
  "code": 0,
  "msg": "注册成功",
  "data": {}
}
```

## 获取用户信息

### 请求URL

`/api/user/info` `GET` `POST`

### 请求参数

| 参数名      | 描述   | 是否必须 | 参考值 |
|----------|------|------|-----|
| uid      | 用户id | 是    ||

### 请求

```json
{
  "uid": "uid"
}
```

### 响应

```json
{
  "code": 0,
  "msg": "获取成功",
  "data": {
    "username": "username",
    "sex": "M",
    "information": "information"
  }
}
```

## 获取驱动器列表

### 请求URL

`/api/driver/list` `GET` `POST`

### 请求参数

| 参数名      | 描述   | 是否必须 | 参考值 |
|----------|------|------|-----|

### 请求

```json
{}
```

### 响应

```json
{
  "code": 0,
  "msg": "获取成功",
  "data": [
    {
      "drive_id": 0,
      "name": "驱动器0",
      "enable_cache": true,
      "auto_refresh_cache": false,
      "enable_search": true,
      "search_ignore_case": true,
      "max_size": 1099511627776,
      "used_size": 209715200,
      "title": "文件路径",
      "value": "/netdisk"
    }
  ]
}
```

## 添加驱动器

### 请求URL

`/api/driver/add` `POST`

### 请求参数

| 参数名                | 描述        | 是否必须 | 参考值               |
|--------------------|-----------|------|-------------------|
| name               | 驱动器名称     | 是    ||
| enable_cache       | 是否开启缓存    | 是    | true 或者 false     |
| auto_refresh_cache | 是否自动刷新缓存  | 是    | true 或者 false     |
| enable_search      | 是否开启搜索    | 是    | true 或者 false     |
| search_ignore_case | 搜索是否忽略大小写 | 是    | true 或者 false     |
| max_size           | 驱动器最大容量   | 是    | 单位为字节             |
| title              | 驱动器地址描述   | 是    ||
| value              | 驱动器地址     | 是    | 绝对地址，比如/usr/local |

### 请求

```json
{
  "name": "驱动器0",
  "enable_cache": true,
  "auto_refresh_cache": false,
  "enable_search": true,
  "search_ignore_case": false,
  "max_size": 1099511627776,
  "title": "文件路径",
  "value": "/netdisk"
}
```

### 响应

```json
{
  "code": 0,
  "msg": "添加成功",
  "data": {}
}
```