import { useState } from 'react';
import Helmet from 'react-helmet';
import classnames from 'classnames';
import { useUser } from 'hooks/User';
import { Link } from 'react-router-dom';
import URLS from 'URLS';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Avatar from '@material-ui/core/Avatar';
import Collapse from '@material-ui/core/Collapse';

// Icons
import EditIcon from '@material-ui/icons/EditRounded';
import AktivitiesIcon from '@material-ui/icons/DateRangeRounded';

// Project Components
import Navigation from 'components/navigation/Navigation';
import Container from 'components/layout/Container';
import Paper from 'components/layout/Paper';
import Tabs from 'components/layout/Tabs';
import Http404 from 'containers/Http404';
import EditProfile from 'containers/Profile/components/EditProfile';
import MyActivities from 'containers/Profile/components/MyActivities';

const BG_IMAGE = 'https://img.mensxp.com/media/content/2020/Apr/Himalayas-Visible-From-Saharanpur-Is-A-Sight-To-Behold1400_5eaaa0a43fb97.jpeg';

const useStyles = makeStyles((theme) => ({
  backgroundImg: {
    background: `${theme.palette.colors.gradient}, url(${BG_IMAGE}) center center/cover no-repeat scroll`,
    width: '100%',
    height: 300,
    backgroundSize: 'cover',
  },
  avatarContainer: {
    position: 'relative',
    margin: theme.spacing('-120px', 'auto', 2),
    zIndex: 2,
    alignItems: 'center',
    [theme.breakpoints.down('lg')]: {
      margin: theme.spacing('-80px', 'auto', 1),
    },
  },
  avatar: {
    background: theme.palette.primary.main,
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
    [theme.breakpoints.down('lg')]: {
      gridTemplateColumns: '1fr',
    },
  },
}));

const Profile = () => {
  const classes = useStyles();
  const { data: user, isLoading, isError } = useUser();
  const activitiesTab = { value: 'activities', label: 'Aktiviteter', icon: AktivitiesIcon };
  const editTab = { value: 'edit', label: 'Rediger profil', icon: EditIcon };
  const tabs = [activitiesTab, editTab];
  const [tab, setTab] = useState(activitiesTab.value);

  if (isError) {
    return <Http404 />;
  }
  if (isLoading || !user) {
    return <Navigation isLoading />;
  }

  return (
    <Navigation maxWidth={false}>
      <Helmet>
        <title>Profil - Gidd</title>
      </Helmet>
      <div className={classes.backgroundImg} />
      <Container className={classnames(classes.grid, classes.root)}>
        <div>
          <Paper className={classnames(classes.grid, classes.avatarContainer)}>
            <Avatar className={classes.avatar}>{`${user.firstName.substr(0, 1)}${user.surname.substr(0, 1)}`}</Avatar>
            <div>
              <Typography align='center' variant='h2'>{`${user.firstName} ${user.surname}`}</Typography>
              <Typography align='center' variant='subtitle2'>
                {user.email}
              </Typography>
            </div>
          </Paper>
          <Button component={Link} fullWidth to={URLS.ADMIN_ACTIVITIES}>
            Administrer aktiviteter
          </Button>
        </div>
        <div className={classes.grid}>
          <Tabs selected={tab} setSelected={setTab} tabs={tabs} />
          <Paper className={classes.grid}>
            <Collapse in={tab === activitiesTab.value}>
              <MyActivities />
            </Collapse>
            <Collapse in={tab === editTab.value} mountOnEnter>
              <EditProfile user={user} />
            </Collapse>
          </Paper>
        </div>
      </Container>
    </Navigation>
  );
};

export default Profile;
