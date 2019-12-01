


create table Department(
 
	did numeric(3, 0),
	dname varchar(30),
	dfloor integer,
	head numeric(5, 0),
	
	primary key(did)
	
);

create table Employee(

	eid numeric(5,0),
	ename varchar(30),
	salary integer,
	did numeric(3,0),
	classification integer check(classification >= 1 and classification <=10),
	
	primary key(eid),
	foreign key(did) references Department(did)

);


create table Budget(

     did numeric(3, 0),
	 byear numeric(4, 0),
	 budget integer,
	 
	 primary key(did, byear),
	 foreign key(did) references Department(did)


);
create table project(

	pid numeric(3, 0),
	pname varchar(30),
	did numeric(3, 0),
	budget integer,
	duedate date,
	
	primary key(pid),
	foreign key(did) references Department(did)


);
create table onproject(

	pid numeric(3, 0),
	eid numeric(5, 0),
	fdate date,

	primary key(pid, eid),
	foreign key(pid) references project(pid),
	foreign key(eid) references Employee(eid)
	
);













