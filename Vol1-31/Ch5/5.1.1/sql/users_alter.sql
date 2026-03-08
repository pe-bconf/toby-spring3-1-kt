alter table users
    add level tinyint not null comment '사용자등급';

alter table users
    add login int not null comment '로그인 횟수';

alter table users
    add recommend int not null comment '추천 횟수';