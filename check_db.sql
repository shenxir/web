USE lostfound_db;
SELECT i.id, i.title, i.category_id, i.location_id, i.phone, i.publish_date, i.expiry_date 
FROM lost_found_item i 
ORDER BY i.id DESC 
LIMIT 5;
