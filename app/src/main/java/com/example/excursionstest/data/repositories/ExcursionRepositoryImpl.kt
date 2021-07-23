package com.example.excursionstest.data.repositories

import com.example.excursionstest.domain.entities.Excursion
import com.example.excursionstest.domain.entities.Step
import com.example.excursionstest.domain.repositories.ExcursionRepository

class ExcursionRepositoryImpl : ExcursionRepository {


    private val step = Step(
        id = 1,
        name = "Знакомьтесь, Исландия!",
        description = "<b>Страна геологических чудес, от которой остается впечатление ожившего канала Nature</b><br><br>" +
                "Ледяные шапки и ледники, бьющие струей гейзеры и испускающие пар сольфатары, вулканы, бурные реки и великолепные водопады, скопления тупиков и гагарок и киты, резвящиеся прямо у берега, — все это лишь очередной день в Исландии. Геологические чудеса этой страны привели к всплеску туризма. Большинство людей, оказывающихся здесь впервые, проезжают по туристическому маршруту Золотое кольцо на юго-западе. Однако в оживленном Рейкьявике хватает и рукотворных достопримечательностей, включая величественную церковь Хатльгримскиркья и никогда не сдающуюся музыкальную среду. Геотермальные источники, такие как богатая минералами Голубая лагуна, тоже привлекают множество посетителей.",
        images = listOf("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/33/f3/7e/caption.jpg?w=1200&h=-1&s=1",
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/01/19/5d/61/waterfall-near-reykiavik.jpg?w=1200&h=-1&s=1",
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/01/2b/da/1f/blue-lagoon.jpg?w=1200&h=-1&s=1",
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/49/ab/9b/photo-by-alisha-bube.jpg?w=1200&h=-1&s=1",
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/17/d4/07/9e/20190606-203104-largejpg.jpg?w=1000&h=-1&s=1",
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/11/2e/dc/06/pingvellir.jpg?w=1200&h=-1&s=1"),
        audio = "/raw/do_i_wanna_know"
    )

    private val excursion = Excursion(
        id = 1,
        name = "Исландия",
        steps = listOf(step)
    )

    override fun getExcursion(): Excursion {
        return excursion
    }

    override fun getStep(): Step {
        return excursion.steps[0]
    }
}