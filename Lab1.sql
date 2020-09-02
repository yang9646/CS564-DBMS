create database university;
show databases;
use university;

create table Class (
Cname varchar(255),
meets_at varchar(255) default 'null',
Room varchar(255) default 'null',
Fid bigint default 0,
primary key (cname));

create table Enroll (
Snum bigint,
Cname varchar(255),
primary key(snum, cname)
);

create table Faculty (
Fid bigint,
Fname varchar(255),
Deptid bigint,
primary key (fid)
);

create table Student (
Snum bigint,
Sname varchar(255),
Major varchar(255),
Level varchar(255),
Age int,
primary key (snum)
);

set global local_infile = true;

/* view tables*/
select* from class;
select * from enroll;
select * from faculty;
select * from student;


/* 1 correct
Find the student id of all students whose name starts with "M"
 */
select snum
from student
where sname like 'M%';

/* 2 correct
Find the name of the classes that meet at room 'R12'
 */
select cname
from class
where room = 'R12';

/* 3 correct
Find the name of all faculty members who are working at department '20'
*/
select fname
from faculty
where deptid = 20;

/* 4 correct
Find the names of all juniors who are currently enrolled in database systems
*/
select s.sname
from student s, enroll e
where s.snum = e.snum
and e.cname = 'Database Systems'
and s.level = 'jr';

/* 5 correct
Find the names of all juniors who are enrolled in a class taught by 'I. Teach'
*/
select s.sname
from student s, class c, faculty f, enroll e
where s.snum = e.snum
and e.cname = c.cname
and c.fid = f.fid
and f.fname = 'I. Teach'
and s.level = 'jr';

/* 6 correct
Find the names of all classes that either meet in room 'R128' or meet MWF
*/
select cname
from class
where meets_at like 'MWF%'
or room = 'r128';

/* 7 correct
Find all the names of all classes taught by Elizabeth Taylor
*/
select c.cname
from class c, faculty f
where c.fid = f.fid
and f.fname = 'elizabeth taylor';

/* 8 correct
Find the names, rooms and schedule of all enrolled classes from joseph thompson
*/
select c.cname, c.room, c.meets_at
from student s, class c, enroll e
where e.snum = s.snum
and e.cname = c.cname
and s.sname = 'joseph thompson';

/* 9 correct
Find the names of all faculty members who teach at 'r128'
*/
select distinct f.fname
from faculty f, class c
where f.fid = c.fid
and c.room = 'r128';

/* 10 correct
Find all the pairs of classes that meet at the same time
(produce pairs in alphabetic order)
*/
select c1.cname as course1, c2.cname as course2
from class c1 join class c2
on c1.meets_at = c2.meets_at
where c1.cname < c2.cname
order by c1.cname;

/* 11 correct
Find the age of the oldest student who is either a History major 
or enrolled in a course taught by I. teach
*/

select max(s.age) as oldest
from class c, enroll e, faculty f, student s 
where 
	s.major - 'history'
    or
	(f.fid = c.fid
		and f.fname = 'I. teach'
		and c.cname = e.cname
		and s.snum = e.snum) ;

/* 12 correct
Find the names of all classes that either meet in room 'r128' 
or have five or more students enrolled
*/
select distinct c.cname
from class c
where c.room = 'r128'
or (select count(e.cname)
	from enroll e
	where e.cname = c.cname
	group by e.cname) > 4;

/* 13 correct
Find the names of all students who are enrolled in two classes that meet at the same time
*/
select s.sname
from student s
where s.snum in 
	(select distinct e1.snum
	from enroll e1, class c1, enroll e2, class c2
	where e1.cname = c1.cname
	and e2.cname = c2.cname
	and e1.snum = e2.snum
	and c1.cname <> c2.cname
	and c1.meets_at = c2.meets_at);
   
/* 14 correct
Find the names of faculty members for whom the combined enrollment of the courses 
that they teach is less than five.
*/

select distinct f.fname
from class c, faculty f
where 5 > 	(select count(e.snum)
			from class c, enroll e
			where c.cname = e.cname
			and c.fid = f.fid
			);
    

/* 15 correct
For each level (FR,SO,JR, SR), print the level and the average age of students 
for that level.
*/
select level, avg(age)
from student
group by level;

/* 16 correct 
For all levels except JR, print the level and the average age of students for that level.
*/
select level, avg(age)
from student
where level <> 'jr'
group by level;

/* 17 correct
For each faculty member that has taught classes only in room “R128”, 
print the faculty member’s name and the total number of classes she or he has taught.
*/
select f1.fname, count(c1.cname) as total
from faculty f1, class c1
where f1.fid not in 
	(select f2.fid
    from faculty f2, class c2
    where f2.fid = c2.fid
    and c2.room <> 'r128')
and f1.fid = c1.fid
group by f1.fname;

/* 18 correct
Find the names of students enrolled in the maximum number of classes. 
*/

select s.sname
from student s
group by s.snum
having (
	select count(*) as num1
	from enroll e1
    where s.snum = e1.snum
	group by e1.snum
    having num1 >= all 
		(select count(*) as num2
		from enroll e2
		group by e2.snum)
        );

/* 19 correct
Find the names of students not enrolled in any class.
*/
select s.sname 
from student s
where s.snum not in
	(select distinct e.snum
    from enroll e );

/* 20 correct
For each age value that appears in Students, 
find the level value that appears most often. 
For example, if there are more FR level students aged 18 than SR, JR, or SO students aged 18, 
you should print the pair (18, FR). 
*/

select s1.age, s1.level
from student s1
group by s1.age, s1.level
having count(s1.level) >= all
	(select count(s2.level)
	from student s2
    where s2.age = s1.age
    group by s2.age, s2.level)
order by s1.age;

/* 21 
Find the average age of students who enroll in classes taught by “I. Teach” 
*/
select avg(c.age) as average
from (select distinct s.sname as sname, s.age as age
	from student s, enroll e, class c, faculty f
	where s.snum = e.snum
		and e.cname = c.cname
		and f.fid = c.fid
        and f.fname = 'I. teach'
	)c;
  