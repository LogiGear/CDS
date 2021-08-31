create table public.departments (
	id int8 not null, 
	created_at timestamp not null, 
	is_deleted BOOLEAN DEFAULT false, 
	updated_at timestamp not null, 
	name varchar(255), 
	manager_id int8, 
	parent_department_id int8, 
	primary key (id));
create table public.employeesdetails (
	id int8 not null, 
	created_at timestamp not null, 
	is_deleted BOOLEAN DEFAULT false, 
	updated_at timestamp not null, 
	address varchar(255), 
	bank_account int8, 
	bank_name varchar(255), 
	birth_day timestamp, 
	cell_phone varchar(255), 
	contracted_date timestamp, 
	degree varchar(255), 
	employeeid int4, 
	full_name varchar(40), 
	gender varchar(10), 
	id_number int8, 
	image varchar(255), 
	insurance_book_no int8, 
	issue_date timestamp, 
	job_title varchar(255), 
	major varchar(255), 
	married_status varchar(255), 
	place_of_birth varchar(255), 
	race varchar(20), 
	religion varchar(20), 
	start_date timestamp, 
	taxid int8, cdm_id int8, 
	department_id int8, 
	manager_id int8, 
	primary key (id));
create table public.projects (
	id int8 not null, 
	created_at timestamp not null, 
	is_deleted BOOLEAN DEFAULT false, 
	updated_at timestamp not null, 
	name varchar(255), 
	primary key (id));
create table public.resumes (
	id int8 not null, 
	name varchar(255), 
	type varchar(255), 
	note varchar(255), 
	status varchar(255), 
	upload_at timestamp, 
	upload_by int8, 
	primary key (id));
create table public.roles (
	id int8 not null,
	created_at timestamp not null, 
	is_deleted BOOLEAN DEFAULT false, 
	updated_at timestamp not null, 
	name varchar(60), 
	primary key (id));
create table public.users (
	id int8 not null, 
	created_at timestamp not null, 
	expired_at timestamp,
	is_deleted BOOLEAN DEFAULT false,
	token_expired_at timestamp, 
	updated_at timestamp not null, 
	email varchar(40), 
	email_token varchar(255), 
	name varchar(255), 
	password varchar(100), 
	password_reset_token varchar(255), 
	status varchar(60), 
	primary key (id));
alter table public.roles add constraint UK_nb4h0p6txrmfc0xbrd1kglp9t unique (name);
alter table public.users add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);
create sequence cv_id_seq start 1 increment 1;
create sequence departments_id_seq start 1 increment 1;
create sequence projects_id_seq start 1 increment 1;
create sequence roles_id_seq start 1 increment 1;
create sequence users_id_seq start 1 increment 1;
create table employeedetails_projects (
	employee_id int8 not null, 
	project_id int8 not null, 
	primary key (employee_id, project_id));
create table users_roles (
	user_id int8 not null, 
	role_id int8 not null, 
	primary key (user_id, role_id));
alter table public.departments add constraint FKppnbnkapy95vngpabqqeftf foreign key (manager_id) references public.employeesdetails;
alter table public.departments add constraint FKf447ltyj9sprmddsd3day3lf3 foreign key (parent_department_id) references public.departments;
alter table public.employeesdetails add constraint FK4lsjvq55klgc3y7vv8ncd8rf8 foreign key (cdm_id) references public.employeesdetails;
alter table public.employeesdetails add constraint FKcybyfr5qymp998f6xng0ev25x foreign key (department_id) references public.departments;
alter table public.employeesdetails add constraint FK6wtj6dc8h8mqvdmueojyfyy3n foreign key (manager_id) references public.employeesdetails;
alter table public.employeesdetails add constraint FKtifu6kou5pyi3qmv7tu63iikv foreign key (id) references public.users;
alter table employeedetails_projects add constraint FKalxf0woy0s2nhgm7k3rebplrf foreign key (employee_id) references public.employeesdetails;
alter table employeedetails_projects add constraint FKrl9qdl4m1ywjg4nnirh54s2xg foreign key (project_id) references public.projects;
alter table users_roles add constraint FKj6m8fwv7oqv74fcehir1a9ffy foreign key (role_id) references public.roles;
alter table users_roles add constraint FK2o0jvgh89lemvvo17cbqvdxaa foreign key (user_id) references public.users;
