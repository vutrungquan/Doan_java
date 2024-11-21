1. App dự báo thời tiết có 2 chức năng chính : Hiển thị thông tin dự báo thời tiết theo thành phố và hiển thị thông tin dự báo thời tiết theo vị trí hiện tại
2. Chức năng hiển thị thông tin dự báo thồi tiết theo vị trí do em ko đăng kí được API để lấy vị trí nên em sử dụng API lấy theo tọa độ nên sẽ có chênh lệch 
về thông tin so với chức năng 1 khi tìm kiếm theo tên thành phố hiện tại 
3. App luôn cập nhật dữ liệu theo thời gian thực và lưu vào db
4. Đối với những thành phố đã được tìm kiếm từ trước khi tìm kiếm lần tiếp theo sẽ đc tự động cập nhật dữ liệu mới vào db và hiển thị ra cho người dùng
còn nếu khi tìm kiếm những thành phố mới chưa có trong db thì sẽ tự động thêm mới và cập nhật dữ liệu mới nhất vào db
5. Lấy thông tin từ API và lọc dữ liệu để hiển thị thời tiết và icon tương ứng
