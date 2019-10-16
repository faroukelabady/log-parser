SELECT 
    ip, COUNT(ip) ip_count
FROM
    `logs`.`log_data`
WHERE
    log_date BETWEEN ? AND ?
GROUP BY ip
HAVING ip_count >= ?;