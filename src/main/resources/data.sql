-- username: ppetar33; passowrd: pera123
insert into users(avatar,email,is_banned,password,registration_date,role,username) values('https://www.w3schools.com/howto/img_avatar.png','ppetar33@gmail.com',false,'$2a$10$HJfLT65hUJLbdayBviGIP.ZT57AiiVCsRgCRy/XnkPJXmm8./rIZS','2020-01-01 12:32:00',2,'ppetar33');
-- username: mmarko33 ; passowrd: mare123
insert into users(avatar,email,is_banned,password,registration_date,role,username) values('https://www.w3schools.com/howto/img_avatar.png','mmarko33@gmail.com',false,'$2a$10$X8/75d3FRPKB78cf4st5VeXmKpAAUr/QPv.4qxyH9FvOJ5Ndjn.Dm','2020-01-01 12:32:00',2,'mmarko33');
-- username: jjovan33; passowrd: joca123
insert into users(avatar,email,is_banned,password,registration_date,role,username) values('https://www.w3schools.com/howto/img_avatar.png','jjovan33@gmail.com',false,'$2a$10$Gxh22mGMAWEZTv/zztdwoO96VjdlFmZKXFNHm51hSicroZCUWsb.K','2020-01-01 12:32:00',2,'jjovan33');
-- username: ddarko33; passowrd: dare123
insert into users(avatar,email,is_banned,password,registration_date,role,username) values('https://www.w3schools.com/howto/img_avatar.png','ddarko33@gmail.com',false,'$2a$10$ax68nTDmHuYuizdcUp.SP.imsgEo2d5Q5BGdvdEucz0G5dJN/Gapa','2020-01-01 12:32:00',0,'ddarko33');
-- username: mmilan33; passowrd: mica123
insert into users(avatar,email,is_banned,password,registration_date,role,username) values('https://www.w3schools.com/howto/img_avatar.png','mmilan33@gmail.com',false,'$2a$10$Wvt7l90yfGcrDkflje.2z.aYJaiK/V2Uvrqm0KnPlb/MNmKCszKhG','2020-01-01 12:32:00',1,'mmilan33');
-- username: sstefan33; passowrd: stefa123
insert into users(avatar,email,is_banned,password,registration_date,role,username) values('https://www.w3schools.com/howto/img_avatar.png','sstefan33@gmail.com',false,'$2a$10$NQ0gQxtQpJSH/w/ymDsXzeEyHUOkc3TlsWNFvU7xI.l6O9n9aQCVK','2020-01-01 12:32:00',1,'sstefan33');
-- username: mmihailo33; passowrd: miki123
insert into users(avatar,email,is_banned,password,registration_date,role,username) values('https://www.w3schools.com/howto/img_avatar.png','mmihailo33@gmail.com',false,'$2a$10$ajOLaGrQ0N3ZowzontXulu24nLQaWLxk5fcVNYtiaM8mfYN05jJXe','2020-01-01 12:32:00',1,'mmihailo33');

insert into subreddit(creation_date,description,is_suspended,name,suspended_reason,posts_count) values('2020-01-01 12:32:00','Basketball is a team sport in which two teams, most commonly of five players each, opposing one another on a rectangular court, compete with the primary objective of shooting a basketball.',false,'Basketball','nema razlog',1);
insert into subreddit(creation_date,description,is_suspended,name,suspended_reason,posts_count) values('2020-01-01 12:32:00','React js is an open-source JavaScript library that is used for building user interfaces specifically for single-page applications.',false,'React','ima razlog',5);

insert into flair(name) values('Programming');
insert into flair(name) values('Sports');
insert into flair(name) values('Television');
insert into flair(name) values('Music');
insert into flair(name) values('Angular');

insert into post(creation_date,image_path,text,vote_count,title,subreddit_subredditid,flair_flairid,user_userid) values('2022-01-01 12:32:00',null,'Nikola Jokic is a Serbian professional basketball player for the Denver Nuggets of the National Basketball Association (NBA) who plays the center position',-1,'Nikola Jokic MVP',1,null,1);
insert into post(creation_date,image_path,text,vote_count,title,subreddit_subredditid,flair_flairid,user_userid) values('2021-01-01 12:32:00',null,'Components are independent and reusable bits of code. They serve the same purpose as JavaScript functions, but work in isolation and return HTML. Components come in two types, Class components and Function components',-2,'React js Components',2,1,1);
insert into post(creation_date,image_path,text,vote_count,title,subreddit_subredditid,flair_flairid,user_userid) values('2018-01-01 12:32:00',null,'Hooks are a new addition in React 16.8. They let you use state and other React features without writing a class',0,'React js hooks',2,1,2);
insert into post(creation_date,image_path,text,vote_count,title,subreddit_subredditid,flair_flairid,user_userid) values('2018-01-01 12:32:00',null,'Hooks are a new addition in React 16.8. They let you use state and other React features without writing a class',2,'React js hooks',2,1,2);
insert into post(creation_date,image_path,text,vote_count,title,subreddit_subredditid,flair_flairid,user_userid) values('2018-01-01 12:32:00',null,'Hooks are a new addition in React 16.8. They let you use state and other React features without writing a class',3,'React js hooks',2,1,2);
insert into post(creation_date,image_path,text,vote_count,title,subreddit_subredditid,flair_flairid,user_userid) values('2018-01-01 12:32:00',null,'Hooks are a new addition in React 16.8. They let you use state and other React features without writing a class',5,'React js hooks',2,1,2);

insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je prvi komentar','2022-01-01 12:32:00',null,1,1,1);
insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je drugi komentar','2021-01-01 12:32:00',null,2,2,0);
insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je treci komentar  Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar Ovo je prvi komentar ','2022-03-01 12:32:00',1,1,3,0);
insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je cetvrti komentar','2022-04-01 12:32:00',3,1,1,0);
insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je peti komentar','2021-01-01 12:32:00',null,2,3,0);
insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je sesti komentar','2022-04-05 12:32:00',1,1,2,0);
insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je sedmi komentar','2017-04-28 12:32:00',null,1,3,0);
insert into comment(is_deleted,text,timestamp,parent_commentid,post_postid,user_userid,vote_count) values(false,'Ovo je osmi komentar','2018-01-01 12:32:00',7,1,2,0);

insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',0,1,null,1);
insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',1,null,1,2);
insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',1,null,1,3);
insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',0,null,1,3);
insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',1,null,2,3);
insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',1,null,2,3);
insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',1,null,2,3);
insert into reaction(timestamp,type,comment_commentid,post_postid,user_userid) values('2020-01-01 12:32:00',0,null,2,3);

insert into report(accepted,report_reason,timestamp,comment_commentid,post_postid,user_userid) values(false,1,'2020-01-01 12:32:00',1,null,1);
insert into report(accepted,report_reason,timestamp,comment_commentid,post_postid,user_userid) values(false,2,'2020-01-01 12:32:00',null,2,1);

insert into subreddit_moderatori(userid,subredditid) values(5,1);
insert into subreddit_moderatori(userid,subredditid) values(6,1);

insert into subreddit_rules(subreddit_subredditid,rules) values(1,'Prvo Pravilo');
insert into subreddit_rules(subreddit_subredditid,rules) values(2,'Drugo Pravilo');
insert into subreddit_rules(subreddit_subredditid,rules) values(1,'Trece Pravilo');
insert into subreddit_rules(subreddit_subredditid,rules) values(2,'Cetvrto Pravilo');

insert into subreddit_flairs(flairid, subredditid) value (1,1);
insert into subreddit_flairs(flairid, subredditid) value (2,1);
insert into subreddit_flairs(flairid, subredditid) value (3,1);
insert into subreddit_flairs(flairid, subredditid) value (4,2);
insert into subreddit_flairs(flairid, subredditid) value (5,2);
insert into subreddit_flairs(flairid, subredditid) value (3,2);

