# Forknife Spatula Royale
## Project report
---
|         Name          | Student # | 
| --------------------- | --------- | 
|Halvor Bakken Smedås   |   473196  |
|Jone Skaara            |   473181  |

### [Forknife Spatula Royale](#forknife-spatula-royale)

#### 1. [The Development Process](#the-development-process)
#### 2. [The Design](#the-design)
#### 3. [What Was The Project About](#what-was-the-project-about)
#### 4. [What Features Are Included](#what-features-are-included)
#### 5. [What Is On Todo (Possible Future Features) List](#what-is-on-todo-possible-future-features-list)
#### 6. [What Was Easy](#what-was-easy)
#### 7. [What Was Hard](#what-was-hard)
#### 8. [What Have We Learnt](#what-have-we-learnt)
#### 9. [Notes](#notes)


---
## The development process
We started out discussing different ideas we found interessting, trying to get a more concrete idea of how they'd work out. After a while we chose to go for one of the ideas: The _Forknife Spatula Royal(TM)_.

The coming days after, we further developed our understanding of the app ide by drawing out concepts to get an united view on what we were going to make.

At this point we had a pretty good idea of what features we wanted, and how we wanted the app to look. We started filling out the layout xml-resource files, getting the basic layout of our planned activities, fragments and menus in place.

Once we'd gotten this far, we made the git repository, and started filling a "scrum-like" board with issues with all the wanted and planned features. Making sure to close issues once they


We made sure to add a "ready"-column for the issues/tasks that were either untouced by other features or had their dependencies fulfiled by implemented features. so we were allways able to pick tasks that could be implemented without taking on other tasks first, making the number of active (/in progress-)items at a low at all times.

Due to the graphics course-project running at the same time as this project, we ended up working on the mobile project mostly in the end of the period designated for it, meaning our workdays for the last weeks were really long. 

Those days were spent working by ourself most the time, but when working with core issues, features, and/or bugs, we did some pair programming to make sure to approach these problems in the best way possible.

## The design

The design is very hevily focused on lists and swipable surfaces, we did this to construct a topological understanding of the app interface for the users. 

This is however untested, so one of our concerns is that our intuition of how our potential users would want interact with the app is wrong. 

| [GIF of recipe logic](https://i.imgur.com/R32hyGa.gifv) |  [GIF of sorting](https://i.imgur.com/yPB4Zud.gifv)
| -------- | -------- |
|[GIF of filter categories](https://i.imgur.com/a6gpaju.gifv)| [GIF of lists](https://i.imgur.com/pA024xS.gifv)     |



## What was the project about
The project was about creating an app for android, good enough that we would actually want to use after the project was done.

We landed on the _Forknife Spatula Royal(TM)_ as we both found the idea compelling and useful.

We feel we've reached that point now with the app, as the most crucial features are in place, making ut useful.

## What features are included
You can:
- You can browse stock recipes.
- Add and remove ingredients from your shopping list(includes searching).
- Fluid transition moving ingredients between shopping list and fridge list.
- Add all ingredients to shopping list that is required by an ingredient.
- Add non-ingredients(like soap and toilet paper) to your shopping list, making sure there's no need for multiple lists across different apps for the users.
- Add and remove ingredients from your fridge list.
- Coherently updated fridge list based on shopping and use of ingredients in recipes(After you make a recipe, you can mark if any ran out. Discard them or and add them to shopping list).
- Sort the recipes on: percentage of the ingredients you have, aphabetical order, and time it takes to make.

## What is on ToDo (possible future features) list
- You can add additional ingredients to an existing recipe to make them more interesting.
- You can share your personal recipes to a public board, and download others.
- Personal recipe list where you add recipes from the global board, or others(shared) recipes.
- Rating system. 
- ~~_**TODO**_~~
## What was easy
Setting up fragments and activities, and designing/writing the xml files.
Making minor changes to existing view types to fulfil our needs. An example of this is our LockedViewPager - As we wanted our lists to be swipable, we couldn't have swipable fragments, which was the case when we first made the viewpager/navigationbar combination in the UI. We made a custom viewpager-class that does not use swipes to be able to control navigation solely on the navigation bar.

## What was hard
Getting initial SQLite tables set up, communicating with the app, and filling them with data. We spent well over a few days trying to get a working database up and running... mostly due to outdated documentation and reccomendations leading us astray.
It was also really difficult to get ADB up and running some times, making it really difficult to validate the database internally. We ended up using online tools for validating our queries rather than getting to see the actual databases in the sqlite shell in ADB.


## What have we learnt
We had great use of the knowledge we got from the database course. Writing the SQL statements and underlying understanding of how SQL works was useful. We learnt more about the different flavours of SQL seing as SQLite has another set of both features and keywords in relation to MySQL.

As our design was very fragment-heavy we learnt more about using them, aswell as learning more about usage of adapters, both for fragments and for different kinds of views (mostly RecyclerView, and it's power)

---
## Notes

We've chosen not to focus on testing of the app, in other means than ourselves actually trying out the features of the app, trying to see if they work as they should and trying to see whether or not they make sense from a users perspective.
In other words, we've simply ran through the added features every time before a major commit to the working branch.
We chose to rather focus on implementing the wanted feature set as time was limited during the end of the project.
If we hadn't been slowed down in such a major way when trying to get the database up and running, we would have implemented some unit tests for the database functionality to make sure it worked every time we made changes to areas in the code touching the database. We evaluated this as very time consuming considering we'd already been spending way too much time on the database by then. 
Undoubtably we could also have added unit test for most of the list-functionality, such as adding/removing/swiping items by validating the changes.

The dataset in the app is very limited. We focused on making the app functional with just sample data to provide the functionality needed, as scraping some recipe data together later on would be eazy enough.
... Or so we thought - As of now: sunday, 6th of may the scraper we tought we'd use is almost done, but when looking through `Matprat.no`'s pages We've discovered that the dataset we were scraping is not free of use, and that we need permission from _Opplysningskontoret for egg og kjøtt_ in Norway to use it. As we won't be able to get that permission before the due date anyways we'll leave it as it is. If [INSERT PROFESSOR NAME HERE] want's to see the app working with a bigger dataset than what's currently in the app, we'd be more than happy to show it using our scraper (written in GoLang) solely for the purpose of showing the general idea of how the dataset would be expanded. 
