create database gameproject;
show databases;
use gameproject;

/* Create table for Game */
create table Game (
    GID BIGINT,
    GameName varchar(135),
    Platform varchar(15) default 'N/A',
    Year varchar(5) default 'N/A',
    Genre varchar(20) default 'N/A',
    primary key (GID, GameName, Platform)
);

    
create table Sales(
    GID BIGINT,
    NA_Sales double default 0.0,
    EU_Sales double default 0.0,
    JP_Sales double default 0.0,
    Other_Sales double default 0.0,
    Global_Sales double default 0.0,
    primary key (GID, Global_Sales),
    foreign key (GID)
		references Game (GID)
        on delete cascade
);

create table Publisher (
    GID BIGINT,
    PublisherName varchar(50),
    primary key (GID),
    foreign key (GID)
		references Game(GID)
        on delete cascade
);

create table Ranking (
    GID BIGINT,
    GameRank BIGINT default 0,
    Global_Sales double,
    primary key (GID),
    foreign key (GID, Global_Sales) 
		references Sales(GID, Global_Sales) 
        on update cascade
        on delete cascade
);

/* Update GameRank of Ranking*/
call updateRank;


/* Update Global_Sales */
delimiter $$
drop procedure if exists updateGlobalSales;
create procedure updateGlobalSales()
begin
	update Sales
    set Global_Sales = round (NA_Sales + EU_Sales + JP_Sales + Other_Sales, 2);
end $$
delimiter ;


/* SEARCH procedure */
delimiter $$
drop procedure if exists searchGame;
create procedure searchGame (in gName varchar(135))
begin
	select r.gamerank, g2.gid, g2.GameName, g2.platform, g2.year, g2.genre, p.publishername, 
		s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales
    from Ranking r, sales s, publisher p, game g2,
        (select g.gid, g.GameName, g.platform, g.year, g.genre 
        from Game g 
        where g.GameName like concat('%',gName,'%'))g1
	where r.gid=s.gid and s.gid=p.gid and p.gid=g2.gid and g2.gid=g1.gid;
end $$
delimiter ;

call searchGame('call of duty:');

/* UPDATE RANK procedure */
delimiter //
drop procedure if exists updateRank;
create procedure updateRank()
begin
	
    drop table if exists temp;
	CREATE TEMPORARY TABLE temp
	select * from
	(select gid,
		row_number() over (
			order by Global_Sales desc
		) r
	
	from ranking
	order by global_sales desc) h;
    
	/* Update ranking*/
	update Ranking
		inner join temp
		on temp.gid = ranking.gid
	set ranking.gamerank= temp.r;
    
    drop table temp;
end //
delimiter ;

/* INSERT procedure*/
delimiter $$
drop procedure if exists insertGame;
create procedure insertGame(in gameName varchar (135), in platform varchar(10), in gameYear varchar (5), 
							in genre varchar(20), in publisherName varchar(40), in NA_Sales double,
							in EU_Sales double, in JP_Sales double, in Other_Sales double)
begin
	
    declare gid BIGINT default (select max(GID) from Game);
    declare global_Sales double default 0.0;
    declare insertGID BIGINT;
    
    /* Assign unique GID for this new data*/
    set gid = gid + 1;
    /* Calculation for global_variables*/
   	set global_Sales = round (NA_Sales + EU_Sales + JP_Sales + Other_Sales, 2);
    
    set insertGID =(
		select GID
		from Ranking r
			natural join
			(select g.gid, g.GameName, g.platform
			from Game g
			where g.GameName = gameName)g1
			natural join
			Sales s
		where g1.GameName = gameName
			and g1.Platform = Platform);
    
	if insertGid is null then
		
		/* Insert data into each table*/
		insert into Game (GID, GameName, Platform, Year, Genre)
			values (gid, gameName, platform, gameYear, genre);
		insert into Publisher (GID, PublisherName)
			values (gid, publisherName);
		insert into Sales (GID, NA_Sales, EU_Sales, JP_Sales, Other_Sales, Global_Sales)
			values (gid, NA_Sales, EU_Sales, JP_Sales, Other_Sales, global_Sales);
		insert into Ranking (GID, GameRank, Global_Sales)
			values (gid, 0, global_Sales);
		
		call updateRank;
	end if;
end $$
delimiter ;


/* DELETE procedure*/
delimiter $$
drop procedure if exists deleteGame;
create procedure deleteGame(in deleteGameName varchar (135),
							in deletePlatform varchar(15))
begin
    declare deleteGid BIGINT default 0;

    set deleteGid =(
		select GID
		from Game
		where GameName = deleteGameName
			and Platform = deletePlatform);
            
	delete 
	from Game
	where gid = deleteGid; 
	
    call updateRank;
	
end $$
delimiter ;

/* Update sales of a game */
delimiter $$
drop procedure if exists updateSales;
create procedure updateSales(in updateGameName varchar(135), in updatePlatform varchar (15), 
							in updateNA double, in updateEU double, in updateJP double, in updateOthers double)
begin
 declare updateGID BIGINT default 0;
	
    set updateGID =(
		select GID
		from Game
		where GameName = updateGameName
			and Platform = updatePlatform);
	
    if updateGID is not null then
        
		update Sales
        set NA_Sales = updateNA,
			EU_Sales = updateEU,
            JP_Sales = updateJP,
            Other_Sales = updateOthers
		where GID = updateGID;
        
		call updateGlobalSales;
        call updateRank;
	end if;
    	
end $$
delimiter ;


/* Display game by rank */
select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, 
	p.publishername, s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales
from Ranking r, Game g, Publisher p, Sales s
where g.GID = r.GID
	and g.GID = p.GID
    and g.GID = s.GID
order by r.gamerank; 	

/* Display top game by platform */
select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, 
	p.publishername, s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, 
	s.global_sales
from Ranking r, Game g, Publisher p, Sales s
where g.GID = r.GID
	and g.GID = p.GID
    and g.GID = s.GID
group by g.platform
having r.gamerank <= all (select r2.gamerank
						from Game g2, Ranking r2
                        where g.platform = g2.platform
                        and g2.GID = r2.GID
                        and g.GID <> g2.GID
                        )
order by g.platform;

/* Display top game by Genre */
select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, 
	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales
from Ranking r, Game g, Publisher p, Sales s
where g.GID = r.GID
	and g.GID = p.GID
    and g.GID = s.GID
group by g.genre
having r.gamerank <= all (select r2.gamerank
						from Game g2, Ranking r2
                        where g.genre = g2.genre
                        and g2.GID = r2.GID
                        and g.GID <> g2.GID
                        )
order by g.genre;

/* Display top game by Year */
select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, 
	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales
from Ranking r, Game g, Publisher p, Sales s
where g.GID = r.GID
	and g.GID = p.GID
    and g.GID = s.GID
group by g.year
having r.gamerank < all (select r2.gamerank
						from Game g2, Ranking r2
                        where g.year = g2.year
                        and g2.GID = r2.GID
                        and g.GID <> g2.GID
                        )
order by g.year desc;


/* Display top game in North America by year */
select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, 
	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales
from Ranking r, Game g, Publisher p, Sales s
where g.GID = r.GID
	and g.GID = p.GID
    and g.GID = s.GID
    and s.NA_Sales > all (select s2.NA_Sales
						from Game g2, Sales s2
                        where g.year = g2.year
							and g2.GID = s2.GID
							and g.GID <> g2.GID
                        )
group by g.year
order by g.year desc;

/* Display top game in Japan by year */
select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername,
	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales
from Ranking r, Game g, Publisher p, Sales s
where g.GID = r.GID
	and g.GID = p.GID
    and g.GID = s.GID
    and s.JP_Sales >= all (select s2.JP_Sales
						from Game g2, Sales s2
                        where g2.GID = s2.GID
							and g.year = g2.year
							and g.GID <> g2.GID
                        )
group by g.year
order by g.year desc;

/* Display top game in Europe by year */
select r.gamerank, g.gid, g.GameName, g.platform, g.year, g.genre, p.publishername, 
	s.na_sales, s.eu_sales, s.jp_sales, s.other_sales, s.global_sales
from Ranking r, Game g, Publisher p, Sales s
where g.GID = r.GID
	and g.GID = p.GID
    and g.GID = s.GID
    and s.EU_Sales >= all (select s2.EU_Sales
						from Game g2, Sales s2
                        where g2.GID = s2.GID
							and g.year = g2.year
							and g.GID <> g2.GID
                        )
group by g.year
order by g.year desc;


