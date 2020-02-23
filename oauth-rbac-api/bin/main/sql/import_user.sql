
insert into rbacdb.oauth_rbac_user(id, username, usertype) values('1','uname1','PEOPLE');
insert into rbacdb.oauth_rbac_user(id, username, usertype) values('2','uname2','FUNCTIONALID');
insert into rbacdb.oauth_rbac_user(id, username, usertype) values('3','uname3','FUNCTIONALID');

insert into rbacdb.oauth_rbac_role(id, rolename) values('1','rname1');
insert into rbacdb.oauth_rbac_role(id, rolename) values('2','rname2');
insert into rbacdb.oauth_rbac_user2role(id, user_id, role_id) values('1','1','1');
insert into rbacdb.oauth_rbac_user2role(id, user_id, role_id) values('2','1','2');
insert into rbacdb.oauth_rbac_user2role(id, user_id, role_id) values('3','1','1');
