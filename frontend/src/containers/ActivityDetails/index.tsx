import { useEffect } from 'react';
import Helmet from 'react-helmet';
import { useParams, useNavigate } from 'react-router-dom';
import URLS from 'URLS';
import { urlEncode } from 'utils';
import { useActivityById } from 'hooks/Activities';
import { makeStyles } from '@material-ui/core/styles';

// Project components
import Http404 from 'containers/Http404';
import Navigation from 'components/navigation/Navigation';
import ActivityRenderer, { ActivityRendererLoading } from 'containers/ActivityDetails/components/ActivityRenderer';
import BACKGROUND from 'assets/img/DefaultBackground.jpg';

const useStyles = makeStyles((theme) => ({
  wrapper: {
    padding: theme.spacing(2),
    [theme.breakpoints.down('md')]: {
      padding: theme.spacing(1),
    },
  },
}));

const ActivityDetails = () => {
  const classes = useStyles();
  const { id } = useParams();
  const { data, isLoading, isError } = useActivityById(id);
  const navigate = useNavigate();

  useEffect(() => {
    if (data) {
      navigate(`${URLS.ACTIVITIES}${id}/${urlEncode(data.title)}/`, { replace: true });
    }
  }, [id, data, navigate]);

  if (isError) {
    return <Http404 />;
  }

  return (
    <Navigation noTransparentTopbar>
      {data && (
        <Helmet>
          <title>{data.title} - GIDD</title>
          <meta content={data.title} property='og:title' />
          <meta content='website' property='og:type' />
          <meta content={window.location.href} property='og:url' />
          <meta content={data.image || 'https://gidd.no' + BACKGROUND} property='og:image' />
        </Helmet>
      )}
      <div className={classes.wrapper}>{isLoading ? <ActivityRendererLoading /> : data !== undefined && <ActivityRenderer data={data} />}</div>
    </Navigation>
  );
};

export default ActivityDetails;
