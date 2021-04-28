import { useEffect, useState } from 'react';
import { urlEncode } from 'utils';
import Helmet from 'react-helmet';
import classnames from 'classnames';
import { useUser, useLogout } from 'hooks/User';
import { Link, useParams, useNavigate } from 'react-router-dom';
import URLS from 'URLS';
import { traningLevelToText } from 'utils';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Avatar from '@material-ui/core/Avatar';
import Collapse from '@material-ui/core/Collapse';

// Icons
import EditIcon from '@material-ui/icons/EditRounded';
import AktivitiesIcon from '@material-ui/icons/DateRangeRounded';
import HistoryIcon from '@material-ui/icons/HistoryRounded';
import PostsIcon from '@material-ui/icons/ViewAgendaRounded';
import LikedIcon from '@material-ui/icons/FavoriteRounded';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Container from 'components/layout/Container';
import Paper from 'components/layout/Paper';
import Tabs from 'components/layout/Tabs';
import Http404 from 'containers/Http404';
import EditProfile from 'containers/Profile/components/EditProfile';
import MyActivities from 'containers/Profile/components/MyActivities';
import MyPosts from 'containers/Profile/components/MyPosts';

import BACKGROUND from 'assets/img/snow_mountains.jpg';

const useStyles = makeStyles((theme) => ({
  backgroundImg: {
    background: `${theme.palette.colors.gradient}, url(${BACKGROUND}) center center/cover no-repeat scroll`,
    width: '100%',
    height: 300,
    backgroundSize: 'cover',
  },
  avatarContainer: {
    position: 'relative',
    marginTop: -120,
    zIndex: 2,
    alignItems: 'center',
    [theme.breakpoints.down('lg')]: {
      marginTop: -80,
    },
  },
  avatar: {
    height: 120,
    width: 120,
    margin: 'auto',
    fontSize: '3rem',
    [theme.breakpoints.down('md')]: {
      height: 100,
      width: 100,
      fontSize: '2rem',
    },
  },
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
    alignItems: 'self-start',
  },
  root: {
    gridTemplateColumns: '300px 1fr',
    gap: theme.spacing(2),
    [theme.breakpoints.down('xl')]: {
      gridTemplateColumns: '250px 1fr',
    },
    [theme.breakpoints.down('lg')]: {
      gridTemplateColumns: '1fr',
    },
  },
  logout: {
    color: theme.palette.error.main,
    borderColor: theme.palette.error.main,
    '&:hover': {
      color: theme.palette.error.light,
      borderColor: theme.palette.error.light,
    },
  },
}));

const Profile = () => {
  const classes = useStyles();
  const { userId }: { userId?: string } = useParams();
  const { data: signedInUser } = useUser();
  const { data: user, isLoading, isError } = useUser(userId);
  const logout = useLogout();
  const posts = { value: 'posts', label: 'Innlegg', icon: PostsIcon };
  const futureActivitiesTab = { value: 'futureActivities', label: 'Kommende aktiviteter', icon: AktivitiesIcon };
  const pastActivitiesTab = { value: 'pastActivities', label: 'Tidligere aktiviteter', icon: HistoryIcon };
  const favouritesTab = { value: 'favourites', label: 'Favoritter', icon: LikedIcon };
  const editTab = { value: 'edit', label: 'Rediger profil', icon: EditIcon };
  const tabs = [posts, futureActivitiesTab, pastActivitiesTab, favouritesTab, ...(userId ? [] : [editTab])];
  const [tab, setTab] = useState(posts.value);
  const navigate = useNavigate();

  useEffect(() => {
    if (user && signedInUser && user.id === signedInUser.id) {
      navigate(`${URLS.PROFILE}`, { replace: true });
    } else if (userId && user) {
      navigate(`${URLS.USERS}${user.id}/${urlEncode(`${user.firstName} ${user.surname}`)}/`, { replace: true });
    }
  }, [user, signedInUser, navigate]);

  if (isError) {
    return <Http404 />;
  }
  if (isLoading || !user) {
    return <Navigation isLoading />;
  }

  return (
    <Navigation maxWidth={false}>
      <Helmet>
        <title>Profil - GIDD</title>
      </Helmet>
      <div className={classes.backgroundImg} />
      <Container className={classnames(classes.grid, classes.root)}>
        <div className={classes.grid}>
          <Paper blurred className={classnames(classes.grid, classes.avatarContainer)}>
            <Avatar className={classes.avatar} src={user.image}>{`${user.firstName.substr(0, 1)}${user.surname.substr(0, 1)}`}</Avatar>
            <div>
              <Typography align='center' variant='h2'>{`${user.firstName} ${user.surname}`}</Typography>
              <Typography align='center' variant='subtitle2'>
                {user.email}
              </Typography>
              <Typography align='center' variant='subtitle2'>
                {traningLevelToText(user.level)}
              </Typography>
            </div>
          </Paper>
          {!userId && (
            <>
              <Button component={Link} fullWidth to={URLS.ADMIN_ACTIVITIES}>
                Administrer aktiviteter
              </Button>
              <Button className={classes.logout} fullWidth onClick={logout} variant='outlined'>
                Logg ut
              </Button>
            </>
          )}
        </div>
        <div className={classes.grid}>
          <Tabs selected={tab} setSelected={setTab} tabs={tabs} />
          <div>
            <Collapse in={tab === posts.value}>
              <MyPosts userId={userId || signedInUser?.id} />
            </Collapse>
            <Collapse in={tab === futureActivitiesTab.value}>
              <MyActivities type='future' userId={userId} />
            </Collapse>
            <Collapse in={tab === pastActivitiesTab.value} mountOnEnter>
              <MyActivities type='past' userId={userId} />
            </Collapse>
            <Collapse in={tab === favouritesTab.value} mountOnEnter>
              <MyActivities type='favourites' userId={userId} />
            </Collapse>
            <Collapse in={tab === editTab.value} mountOnEnter>
              <Paper>
                <EditProfile user={user} />
              </Paper>
            </Collapse>
          </div>
        </div>
      </Container>
    </Navigation>
  );
};

export default Profile;
