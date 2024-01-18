import './App.css';
import {HashRouter as Router, Route, Switch, Redirect} from "react-router-dom";
import Home from './views/Home/Home'
import Editor from './views/Editor/Editor'
import BlogDetail from './views/BlogDetail/BlogDetail'
import User from "./views/User/User";
import Forum from "./views/Forum/Forum";
import NotFound from "./views/NotFound/NotFound";
function App() {
  return (
      <Router>
          <Switch>
              <Route exact path="/index" component={Home}></Route>
              <Route exact path="/editor/:operate/:id" component={Editor}></Route>
              <Route exact path="/blog/detail/:params" component={BlogDetail}></Route>
              <Route exact path="/user/:params" component={User}></Route>
              <Route exact path="/forum" component={Forum}></Route>
              <Route exact path="/" render={() => <Redirect to="/index" />} />
              <Route path="/" component={NotFound}></Route>
          </Switch>
      </Router>
  );
}

export default App;
