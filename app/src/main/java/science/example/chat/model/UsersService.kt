package science.example.chat.model

import com.github.javafaker.Faker

typealias UsersListener = (users: List<User>) -> Unit


//управление данными
class UsersService {

    //список пользователей
    private var users: MutableList<User> = mutableListOf<User>()
    //инициализация списка пользователей

    private val listeners: MutableSet<UsersListener> = mutableSetOf<UsersListener>()

    init {
        val faker = Faker.instance()
        //всего 100 пользователей
        //id-число от 1 до 100 преобразованное в лонг
        //name-генерируем случайное имя пользователя при помощи библиотеки
        //company-случайная компания с помощью библиотеки
        IMAGES.shuffle()
        users = (1..100).map { User(
            id = it.toLong(),
            name = faker.name().name(),
            company = MESSAGE[it % MESSAGE.size],
            photo = IMAGES[it % IMAGES.size],
            unread = UNREAD.random()
        ) }.toMutableList()
    }


    //getUsers - получение списка пользователей
    fun getUsers(): List<User> {
        return users
    }

    //удаление пользователя
    fun deleteUser(user: User) {
        //проверка совпадения идентификатора пользователя со списком
        val indexToDelete: Int = users.indexOfFirst {it.id == user.id}
        //если индекс не равен -1, то удаляем пользователя с таким индексом
        if (indexToDelete != -1) {
            users = ArrayList(users)
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    }


    //добавить
    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        listener.invoke(users)
    }

    //удалять
    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach { it.invoke(users) }
    }
    companion object{
        private val IMAGES = mutableListOf(
            "https://bigpicture.ru/wp-content/uploads/2019/04/grandbeauty00.jpg",
            "https://www.fotoprizer.ru/img_inf/st_221.jpg",
            "https://mixnews.lv/wp-content/uploads/2020/04/20/2020-04-20-mixnews-grandbeauty07.jpg",
            "https://birdinflight.imgix.net/wp-content/uploads/2017/07/Elliott-Verdier-5-1-2.jpg?fm=pjpg&q=70&fit=crop&crop=faces&w=690",
            "https://s9.travelask.ru/system/images/files/001/283/550/wysiwyg_jpg/6587.jpg?1551816385",
            "data:image/jpeg;base64,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",
            "https://avatars.mds.yandex.net/get-zen_doc/1132604/pub_5e59063c27d959242da82cdf_5e59066fd60bd920d8b69a64/scale_1200",
            "https://htstatic.imgsmail.ru/pic_image/3f7393061bbe8f79ff66375c56c695d1/840/549/1676477/",
            "https://icdn.lenta.ru/images/2019/11/16/11/20191116111842532/square_320_9ed774893e973dc06e7e118094e04f1a.jpg"
        )

        private val MESSAGE = mutableListOf(
            "Привет!",
            "Как дела?",
            "Какие планы на сегодня?",
            "Сколько я тебе должен?",
            "Увидимся?",
            "Я прочитала книгу,которую ты советовала. Я считаю, что это слишком откровенная история.",
            "Купил билеты. Завтра вылетаем, надеюсь ты уже собралась)",
            "Никогда не думал, что астрономия это настолько интересно. Вчера весь вечер просидел на крыше и не мог оторваться",
            "Возможно когда нибудь она закончит эту работу, ее уже ждет куча новых задач, не понимаю сколько можно тянуть",
            "У нас бешеная защита, не забывайте",
            "Я навела справки\n" +
                    "Вот примерная команда СВОИ с которыми мы будем играть))\n" +
                    "Катя должна их вроде знать....\n" +
                    "Ну, я думаю, по силам вообще) настраиваемся))",
            "У нас по составу только поменялись Света и Катя?",
            "Хотелось конечно лучше ни при том что мы вместе тренировались только 2-3 человека из состава, отличный результат",
            "Хорошо, без проблем) Даже если вдруг Антон куда-то уедет по делам, я дома работать буду")

        private val UNREAD = mutableListOf(
            (0..1).random(),
            (0..2).random(),
            (0..3).random(),
            (0..4).random(),
            (0..5).random(),
            (1..1).random(),
            (1..2).random(),
            (1..3).random(),
            (1..4).random(),
            (1..5).random(),
            (0..1).random(),
            (0..2).random(),
            (0..3).random(),
            (0..4).random(),
            (0..5).random(),
            (1..1).random(),
            (1..2).random(),
            (1..3).random(),
            (1..4).random(),
            (1..5).random()
        )
    }

}