insert into system_user (user_name, password, is_enabled) values ('user', '123', 1);

insert into system_user (user_name, password, is_enabled) values ('createUser', '123', 1);
select @user_id := last_insert_id();
insert into system_user_authority (permission, content_type, system_user_id) values
  ('CREATE', 'PERSON', @user_id),
  ('CREATE', 'PROJECT', @user_id),
  ('CREATE', 'MEMBERSHIP', @user_id);

insert into system_user (user_name, password, is_enabled) values ('updateUser', '123', 1);
select @user_id := last_insert_id();
insert into system_user_authority (permission, content_type, system_user_id) values
  ('UPDATE', 'PERSON', @user_id),
  ('UPDATE', 'PROJECT', @user_id),
  ('UPDATE', 'MEMBERSHIP', @user_id);

insert into system_user (user_name, password, is_enabled) values ('deleteUser', '123', 1);
select @user_id := last_insert_id();
insert into system_user_authority (permission, content_type, system_user_id) values
  ('DELETE', 'PERSON', @user_id),
  ('DELETE', 'PROJECT', @user_id),
  ('DELETE', 'MEMBERSHIP', @user_id);
