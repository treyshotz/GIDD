import Helmet from 'react-helmet';
import classnames from 'classnames';
import { useUser } from 'hooks/User';
import { Link, Routes, Route, Outlet } from 'react-router-dom';
import URLS from 'URLS';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Avatar from '@material-ui/core/Avatar';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Container from 'components/layout/Container';
import Paper from 'components/layout/Paper';
import Http404 from 'containers/Http404';
import EditProfile from 'containers/Profile/components/EditProfile';
import MyActivities from 'containers/Profile/components/MyActivities';

const useStyles = makeStyles((theme) => ({
  overlayContainer: {
    position: 'relative',
  },
  overlay: {
    position: 'absolute',
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
    zIndex: 1,
    background: `linear-gradient(to top, transparent 70%, ${theme.palette.colors.topbar})`,
  },
  backgroundImg: {
    width: '100%',
    height: 300,
    objectFit: 'cover',
    filter: 'brightness(1.3)',
  },
  avatarContainer: {
    position: 'relative',
    margin: '-170px auto auto',
    zIndex: 2,
    gridTemplateColumns: 'auto 1fr',
    alignItems: 'center',
    [theme.breakpoints.down('md')]: {
      margin: '-80px auto auto',
      gridTemplateColumns: 'auto 1fr',
    },
  },
  avatar: {
    background: theme.palette.primary.main,
    height: 120,
    width: 120,
    margin: 'auto',
    fontSize: '3rem',
    [theme.breakpoints.down('md')]: {
      height: 70,
      width: 70,
      fontSize: '1.5rem',
    },
  },
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  content: {
    paddingTop: theme.spacing(1),
    paddingBottom: theme.spacing(1),
    gridTemplateColumns: '1fr 1fr',
    [theme.breakpoints.down('md')]: {
      gridTemplateColumns: '1fr',
    },
  },
  buttons: {
    margin: theme.spacing(2, 'auto'),
    gridTemplateColumns: '1fr 1fr',
  },
}));

const EDIT_PROFILE_URL = 'rediger/';

const Profile = () => {
  const classes = useStyles();
  const { data: user, isLoading, isError } = useUser();

  if (isError) {
    return <Http404 />;
  }
  if (isLoading || !user) {
    return <Navigation isLoading />;
  }

  const Content = () => (
    <>
      <Container className={classnames(classes.grid, classes.buttons)} maxWidth='md'>
        <Button component={Link} to={EDIT_PROFILE_URL}>
          Oppdater profil
        </Button>
        <Button component={Link} to={URLS.ADMIN_ACTIVITIES}>
          Administrer aktiviteter
        </Button>
      </Container>
      <Container className={classnames(classes.grid, classes.content)}>
        <div className={classes.grid}>
          <Typography variant='h3'>Kommende aktiviteter</Typography>
          <Paper className={classes.grid}>
            <MyActivities />
          </Paper>
        </div>
      </Container>
    </>
  );

  return (
    <Navigation maxWidth={false}>
      <Helmet>
        <title>Profil - Gidd</title>
      </Helmet>
      <div className={classes.overlayContainer}>
        <div className={classes.overlay} />
        <img
          className={classes.backgroundImg}
          src='https://img.mensxp.com/media/content/2020/Apr/Himalayas-Visible-From-Saharanpur-Is-A-Sight-To-Behold1400_5eaaa0a43fb97.jpeg'
        />
      </div>
      <Container maxWidth='md'>
        <Paper className={classnames(classes.grid, classes.avatarContainer)}>
          <Avatar className={classes.avatar}>{`${user.first_name.substr(0, 1)}${user.surname.substr(0, 1)}`}</Avatar>
          <div>
            <Typography variant='h2'>{`${user.first_name} ${user.surname}`}</Typography>
            <Typography variant='subtitle2'>{user.email}</Typography>
          </div>
        </Paper>
      </Container>
      <Routes>
        <Route element={<EditProfile user={user} />} path={EDIT_PROFILE_URL} />
        <Route element={<Content />} path='*' />
      </Routes>
      <Outlet />
    </Navigation>
  );
};

export default Profile;
