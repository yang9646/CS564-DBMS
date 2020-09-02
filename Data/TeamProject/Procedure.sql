/*
makeTempUpdate creates a temp table used to find the new rank of a game
*/
delimiter $$
drop procedure if exists makeTempUpdate;
create procedure makeTempUpdate()
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
end $$
delimiter ;

/*
updateRank updates the gamerank of a game based on its global sales data
*/
delimiter //
drop procedure if exists updateRank;
create procedure updateRank()
begin
	/*Make temp table*/
    call makeTempUpdate;

	/* Update ranking*/
	update Ranking
		inner join temp
		on temp.gid = ranking.gid
	set ranking.gamerank= temp.r;
    
    drop table temp;
end //
delimiter ;