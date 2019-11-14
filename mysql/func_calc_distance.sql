/**
 * 根据地图上两个点的坐标计算两点距离
 * @return 返回距离单位为(米)
 */
DELIMITER $$
DROP FUNCTION IF EXISTS func_calc_distance$$
CREATE FUNCTION func_calc_distance(
	targetLongitude DECIMAL(10,6),	-- 目的地经度
	targetLatitude DECIMAL(10,6),	-- 目的地纬度
	currentLongitude DECIMAL(10,6), -- 当前地点经度
	currentLatitude DECIMAL(10,6))	-- 当前地点纬度
RETURNS DOUBLE
BEGIN
	DECLARE result DOUBLE DEFAULT 0;
	SET result = ROUND(6378.138*2*ASIN(SQRT(POW(SIN((targetLatitude*PI()/180-currentLatitude*PI()/180)/2),2)+COS(targetLatitude*PI()/180)*COS(currentLatitude*PI()/180)*POW(SIN((targetLongitude*PI()/180-currentLongitude*PI()/180)/2),2)))*1000);
	RETURN result;      
END$$
DELIMITER ;