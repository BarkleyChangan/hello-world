* 自连接删除重复的行

  ```mysql
  DELETE FROM Products p1
        WHERE id < (SELECT MAX(P2.id) FROM Products p2 
                     WHERE p1.name=p2.name AND p1.price=p2.price);
  ```

* 自连接Rank排序

  ```mysql
  -- 排序从 1 开始。如果已出现相同位次，则跳过之后的位次 
  SELECT p1.name,p1.price,(SELECT COUNT(P2.price) FROM Products p2
                            WHERE p2.price>p1.price)+1 AS rank_1
    FROM Products 1 
    ORDER BY rank_1;
  ```

  

