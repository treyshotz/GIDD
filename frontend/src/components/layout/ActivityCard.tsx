// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Card, CardActionArea, CardContent, CardMedia, Typography } from '@material-ui/core/';

// Project Components
import { Link } from 'react-router-dom';
import URLS from 'URLS';
import { ActivityList } from 'types/Types';

// Images
import LOGO from 'assets/img/DefaultBackground.jpg';

const useStyles = makeStyles((theme) => ({
  root: {
    marginBottom: theme.spacing(1),
  },
  media: {
    height: 140,
  },
  link: {
    textDecoration: 'none',
  },
  description: {
    overflow: 'hidden',
    '-webkit-line-clamp': 4,
    display: '-webkit-box',
    '-webkit-box-orient': 'vertical',
    whiteSpace: 'break-spaces',
  },
}));

export type ActivityCardProps = {
  activity: ActivityList;
};

export default function ActivityCard(props: ActivityCardProps) {
  const classes = useStyles();

  return (
    <Link className={classes.link} to={`${URLS.ACTIVITIES}${props.activity.id}/`}>
      <Card className={classes.root}>
        <CardActionArea>
          <CardMedia className={classes.media} image={LOGO} />
          <CardContent>
            <Typography component='h2' gutterBottom variant='h5'>
              {props.activity.title}
            </Typography>
            <Typography className={classes.description} variant='body2'>
              {props.activity.description}
            </Typography>
          </CardContent>
        </CardActionArea>
      </Card>
    </Link>
  );
}
