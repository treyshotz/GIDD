import { Link, Routes, Route, Outlet } from 'react-router-dom';
import { AUTH_RELATIVE_ROUTES } from 'URLS';
import Helmet from 'react-helmet';
import URLS from 'URLS';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';

// Project Components
import Logo from 'components/miscellaneous/Logo';
import Login from 'containers/Auth/components/Login';
import SignUp from 'containers/Auth/components/SignUp';
// import ForgotPassword from 'containers/Auth/components/ForgotPassword';

const useStyles = makeStyles((theme) => ({
  wrapper: {
    minHeight: '100vh',
    display: 'grid',
    gridTemplateColumns: '600px 1fr',
    [theme.breakpoints.down('xl')]: {
      gridTemplateColumns: '500px 1fr',
    },
    [theme.breakpoints.down('lg')]: {
      gridTemplateColumns: '1fr',
    },
  },
  image: {
    [theme.breakpoints.up('lg')]: {
      background: `url('https://img3.goodfon.com/wallpaper/nbig/4/17/priroda-devushka-beg-sport.jpg')`,
      backgroundPosition: 'center',
      backgroundAttachment: 'fixed',
      backgroundSize: 'cover',
    },
    [theme.breakpoints.down('lg')]: {
      background: theme.palette.colors.topbar,
      position: 'absolute',
      height: 115,
      top: 0,
      left: 0,
      right: 0,
      zIndex: -1,
    },
  },
  root: {
    maxWidth: theme.breakpoints.values.sm,
    width: '100%',
    margin: theme.spacing(4, 'auto'),
    padding: theme.spacing(1),
  },
  logoContainer: {
    height: 50,
    marginBottom: theme.spacing(5),
  },
  logo: {
    width: 'auto',
    margin: 0,
  },
}));
const Auth = () => {
  const classes = useStyles();
  return (
    <div className={classes.wrapper}>
      <Helmet>
        <title>Logg inn</title>
      </Helmet>
      <div className={classes.image} />
      <div className={classes.root}>
        <div className={classes.logoContainer}>
          <Logo className={classes.logo} size='large' />
        </div>
        <Routes>
          {/* <Route element={<ForgotPassword />} path={AUTH_RELATIVE_ROUTES.FORGOT_PASSWORD} /> */}
          <Route element={<SignUp />} path={AUTH_RELATIVE_ROUTES.SIGNUP} />
          <Route element={<Login />} path='*' />
        </Routes>
        <Outlet />
        <Button color='primary' component={Link} fullWidth to={URLS.LANDING} variant='text'>
          Til forsiden
        </Button>
      </div>
    </div>
  );
};

export default Auth;
