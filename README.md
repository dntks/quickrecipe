# quickrecipe
Recipe search app using spoonacular API

Instructions to build:
In Android studio it should run as is with the default gradle app configuration.

Architecture and design decisions:
The app follows a simple layered architecture, having a data layer with a remote and local
datasrouce,
abstracting them with a repository. The repository is connected by use cases, which are used by the
view models,
which contain the states for the UI.  
The QuickRecipeRepository is responsible for deciding which data source to use, based on whether the
RemoteDataSource runs to an error or not. It's a simplification to avoid complex logic,
however it can bring up some problems, as there's no retry mechanism built in the app currently.
I decided to use Dagger/Hilt for dependency injection, having two simple modules, one for the
database related objects
and one for the networking ones. The use cases became quite simple, as no complex business logic is
involved,
as most of the logic is about deciding which data source is to be used, and how to update the UI
state.

Regarding the task I spent some time thinking about how to implement the two different searches,
and in the end I decided that the user can switch between the recipe title and ingredient search,
as they need different API calls, and after trying different ways of calling the backend, I couldn't
make
it work to have them in one request. Another way I was considering was to have two backend calls at
the same
time, and merging the results afterwards, but with pagination it would have caused more problems.
Regarding pagination I choose the simplest way, if the data is retrieved from the database, then I
ignore pagination,
otherwise I'm increasing the offset when the data is not being loaded and the Lazycolumn scrolling
is near to the last element.

With the unit tests I covered most of the cases where logic is involved, from the viewmodels to the
data sources I covered as much as time allowed.
