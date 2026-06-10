class Main {
    public static void main(String[] args) {
        BookDao bookDao = new BookDaoImpl();
        Book book1 = new Book(1, "Cpp", "Abc");
        Book book2 = new Book(2, "Java", "Def");
        Book book3 = new Book(3, "Python", "Ghi");
        Book book4 = new Book(4, "Java", "Jkl");
        bookDao.save(book1);
        bookDao.save(book2);
        bookDao.save(book3);
        bookDao.save(book4);
        System.out.println(bookDao.findById(2));
        bookDao.deleteById(3);
        System.out.println(bookDao.findAllBooks());
        bookDao.update(new Book(2, "Java", "Mno"));
    }
}